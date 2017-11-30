package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.PublicKeyApi;
import io.pestakit.users.api.model.PublicKey;
import io.pestakit.users.entities.PublicKeyEntity;
import io.pestakit.users.repositories.PublicKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class PublicKeyApiController implements PublicKeyApi {


    @Autowired
    private PublicKeyRepository keyRepo;

    @Override
    public ResponseEntity<PublicKey> getPublicKey() {
        PublicKey key = null;
        for (PublicKeyEntity pk : keyRepo.findAll()) {
            key = toPublicKey(pk);
        }
        return ResponseEntity.ok(key);
    }

    private PublicKey toPublicKey(PublicKeyEntity keyEntity) {
        PublicKey key = new PublicKey();
        key.setKey(keyEntity.getPublicKey());
        key.setDateCreation(keyEntity.getDateCreation());
        key.setDateExpiration(keyEntity.getDateExpiration());
        return key;
    }
}
