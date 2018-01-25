package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.PublicKeyApi;
import io.pestakit.users.api.endpoints.security.JwtService;
import io.pestakit.users.repositories.PublicKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class PublicKeyApiController implements PublicKeyApi {


    @Autowired
    private PublicKeyRepository keyRepo;
    @Autowired
    JwtService jwtService;

    @Override
    public ResponseEntity<String> getPublicKey() {
        String key = jwtService.getPublicKey();
        return ResponseEntity.ok(key);
    }

//    private PublicKey toPublicKey(PublicKeyEntity keyEntity) {
//        PublicKey key = new PublicKey();
//        key.setKey(keyEntity.getPublicKey());
//        key.setDateCreation(keyEntity.getDateCreation());
//        key.setDateExpiration(keyEntity.getDateExpiration());
//        return key;
//    }
}
