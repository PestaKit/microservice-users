package io.pestakit.users.api.endpoints.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import sun.security.rsa.RSAKeyPairGenerator;

import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtService {
    private final long DEFAULT_EXPIRATION = TimeUnit.DAYS.toMillis(7); // 1 week
    private final String PRIV_KEY_FILENAME = "private_key.pem";
    private final String PUB_KEY_FILENAME = "public_key.pem";

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private String publicKeyPem;

    private JWTVerifier verifier;

    private JwtService() {
        try {
            privateKey = (RSAPrivateKey)readPubFile(PRIV_KEY_FILENAME);
            publicKey = (RSAPublicKey)readPubFile(PUB_KEY_FILENAME);
            publicKeyPem = writePub(publicKey, "PUBLIC KEY").toString();
        } catch (InvalidKeySpecException | IOException e) {
            generateKeys();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        verifier = JWT.require(algorithm)
                .build();
    }

    private void generateKeys() {
        KeyPair kp = new RSAKeyPairGenerator().generateKeyPair();
        privateKey = (RSAPrivateKey)kp.getPrivate();
        publicKey = (RSAPublicKey)kp.getPublic();

        try {
            ByteArrayOutputStream privKeyBaos = writePub(privateKey, "RSA PRIVATE KEY");
            ByteArrayOutputStream pubKeyBaos = writePub(publicKey, "PUBLIC KEY");

            writeFile(privKeyBaos, PRIV_KEY_FILENAME);
            writeFile(pubKeyBaos, PUB_KEY_FILENAME);

            publicKeyPem = pubKeyBaos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void writeFile(ByteArrayOutputStream baos, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        try {
            fos.write(baos.toByteArray());
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

    public String getPublicKey() {
        return publicKeyPem;
    }

    public String create(SessionPayload payload) {
        return create(payload, DateTime.now().plus(DEFAULT_EXPIRATION).toDate());
    }

    public String create(SessionPayload payload, Date expiration) {
        String token = null;

        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            token = JWT.create()
                    .withExpiresAt(expiration)
                    .withClaim("userID", payload.getUserId().toString())
                    .sign(algorithm);
        } catch (JWTCreationException e){
            e.printStackTrace();
        }

        return token;
    }

    public SessionPayload verify(String token) throws JWTVerificationException {
        return new SessionPayload(verifier.verify(token).getClaim("userID").asLong());
    }

    public static class SessionPayload {
        private UUID userId;

        public SessionPayload(UUID userId) {
            this.userId = userId;
        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }
    }
}
