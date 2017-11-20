package io.pestakit.users.repositories;

import io.pestakit.users.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsernameIgnoreCase(String username);
}
