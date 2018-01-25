package io.pestakit.users.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

@Component
public class JwtService {
    private final String PUB_KEY_FILENAME = "public_key.pem";
    private final String ENDPOINT = "/publicKey";
    private String apiUrl;

    private RSAPublicKey publicKey;

    private JWTVerifier verifier;

    @Autowired
    private JwtService(@Value("${users.security.api.url}") String apiUrl) {
        this.apiUrl = apiUrl;

        try {
            String remoteCert = getRemoteCertificate();
            if(remoteCert != null)
                writeFile(remoteCert, PUB_KEY_FILENAME);

            publicKey = (RSAPublicKey) readPubFile(PUB_KEY_FILENAME);
        } catch (IllegalArgumentException | InvalidKeySpecException | IOException | NoSuchAlgorithmException e) {
            System.err.println("Unable to get/load public key, please verify the api url and your connection");
        }

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        verifier = JWT.require(algorithm)
                .build();
    }

    private String getRemoteCertificate() {
        RestTemplate rest = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>("", new HttpHeaders());
        try {
            ResponseEntity<String> responseEntity = rest.exchange(apiUrl + ENDPOINT, HttpMethod.GET, requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful())
                return responseEntity.getBody();
        }
        catch (ResourceAccessException e) {}

        return null;
    }

    private Key readPubFile(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader pr = new PemReader(new FileReader(filename));
        Key key;
        try {
            PemObject po = pr.readPemObject();
            KeyFactory kf = KeyFactory.getInstance("RSA");
            if(po.getType().equals("RSA PRIVATE KEY"))
                key = kf.generatePrivate(new PKCS8EncodedKeySpec(po.getContent()));
            else
                key = kf.generatePublic(new X509EncodedKeySpec(po.getContent()));
        }
        finally {
            pr.close();
        }
        return key;
    }

    private void writeFile(String content, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        try {
            fos.write(content.getBytes());
        }
        finally {
            fos.close();
        }
    }

    private ByteArrayOutputStream writePub(Key key, String description) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PemObject po = new PemObject(description, key.getEncoded());
        PemWriter pw = new PemWriter(new OutputStreamWriter(baos));
        try {
            pw.writeObject(po);
        } finally {
            pw.close();
        }

        return baos;
    }

    public UserProfile verify(String token) throws JWTVerificationException {
        DecodedJWT decJwt = verifier.verify(token);
        return new UserProfile(UUID.fromString(decJwt.getClaim("userID").asString()), decJwt.getClaim("username").asString());
    }
}
