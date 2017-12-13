package io.pestakit.users.api.endpoints;

import com.auth0.jwt.exceptions.JWTVerificationException;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.pestakit.users.api.AuthApi;
import io.pestakit.users.api.model.Credentials;
import io.pestakit.users.api.model.Token;
import io.pestakit.users.configuration.SessionManager;
import io.pestakit.users.entities.UserEntity;
import io.pestakit.users.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class AuthApiController implements AuthApi {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<Token> auth(@ApiParam(value = "", required = true) @Valid @RequestBody Credentials cred) {
        UserEntity user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(cred.getIdentifier(), cred.getIdentifier());

        if(user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Argon2 argon2 = Argon2Factory.create();

        // Verify password
        if(!argon2.verify(user.getPassword(), cred.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Token response = new Token();

        SessionManager sm = SessionManager.getInstance();
        SessionManager.SessionPayload sp = new SessionManager.SessionPayload(user.getId());
        String token = sm.create(sp);
        response.setToken(token);

        try {
            String test = sm.verify(token);
            System.out.println(test);
        }
        catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(response);
    }
}
