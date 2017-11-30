package io.pestakit.users.repositories;

import io.pestakit.users.entities.PublicKeyEntity;
import org.springframework.data.repository.CrudRepository;

public interface PublicKeyRepository extends CrudRepository<PublicKeyEntity, Long> {
}
