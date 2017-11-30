package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.PublicKeyApi;
import io.pestakit.users.configuration.SessionManager;
import io.pestakit.users.repositories.PublicKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;


@Controller
public class PublicKeyApiController implements PublicKeyApi {


    @Autowired
    private PublicKeyRepository keyRepo;

    @Override
    public ResponseEntity<String> getPublicKey() {
        SessionManager sm = SessionManager.getInstance();
        String key = sm.getPublicKey();
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
