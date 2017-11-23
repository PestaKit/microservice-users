package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.PublicKeyApi;
import io.pestakit.users.api.model.PublicKey;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@Controller
public class PublicKeyApiController implements PublicKeyApi {


    private PublicKey key;

    @Override
    public ResponseEntity<PublicKey> getPublicKey() {
        return ResponseEntity.ok(key);
    }
}
