package io.pestakit.users.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
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
import java.util.Date;

public class SessionManager {
    private final String PRIV_KEY_FILENAME = "private_key.pem";
    private final String PUB_KEY_FILENAME = "public_key.pem";

    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private String publicKeyPem;

    private SessionManager() {
        try {
            privateKey = (RSAPrivateKey)readPubFile(PRIV_KEY_FILENAME);
            publicKey = (RSAPublicKey)readPubFile(PUB_KEY_FILENAME);
            publicKeyPem = writePub(publicKey, "PUBLIC KEY").toString();
        } catch (InvalidKeySpecException | IOException e) {
            generateKeys();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
            key = kf.generatePrivate(new PKCS8EncodedKeySpec(po.getContent()));
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

    public String create(SessionPayload payload, Date expiration) {
        ObjectMapper mapper = new ObjectMapper();
        String token = null;

        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            token = JWT.create()
                    .withIssuer("user-api")
                    .withExpiresAt(expiration)
                    .withSubject(mapper.writeValueAsString(payload))
                    .sign(algorithm);
        } catch (JWTCreationException | JsonProcessingException e){
            e.printStackTrace();
        }

        return token;
    }

    public static class SessionPayload {
        private long userId;

        public SessionPayload(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }
}
