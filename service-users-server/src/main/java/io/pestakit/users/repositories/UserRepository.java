package io.pestakit.users.repositories;

import io.pestakit.users.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsernameIgnoreCase(String username);
    UserEntity findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    UserEntity findById(UUID id);
}
