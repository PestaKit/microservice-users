package io.pestakit.users.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sun.security.rsa.RSAKeyPairGenerator;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    final private RSAPrivateKey privateKey;
    final private RSAPublicKey publicKey;

    private SessionManager() {
        KeyPair kp = new RSAKeyPairGenerator().generateKeyPair();
        privateKey = (RSAPrivateKey)kp.getPrivate();
        publicKey = (RSAPublicKey)kp.getPublic();

        System.out.println(publicKey);
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
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
        } catch (JWTCreationException e){
            e.printStackTrace();
        } catch (JsonProcessingException e) {
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
