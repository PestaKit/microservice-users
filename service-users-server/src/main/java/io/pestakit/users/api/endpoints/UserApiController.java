package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.UsersApi;
import io.pestakit.users.api.model.User;
import io.pestakit.users.entities.UserEntity;
import io.pestakit.users.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserApiController implements UsersApi {


    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<Void> createUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {

        //if the ressource already exist
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (userEntity != null) {
            //can't create the object
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //if all condition is plain we can now create the object
        UserEntity newUserEntity = toUserEntity(user);
        userRepository.save(newUserEntity);
        Long id = newUserEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUserEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            users.add(toUser(userEntity));
        }

        return ResponseEntity.ok(users);
    }

    @Override
     public ResponseEntity<User> getUser(@ApiParam(value = "user's id",required=true )
                                             @PathVariable("id") Long id) {
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity == null) {
            return ResponseEntity.notFound().build();
        }
        User user = toUser(userEntity);

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> getUser( @NotNull @ApiParam(value = "user's username", required = true)
                                  @RequestParam(value = "username", required = true) String username) {
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(username);
        if (userEntity != null)  {
                return ResponseEntity.ok(toUser(userEntity));
            }

        return ResponseEntity.notFound().build();
    }

    private UserEntity toUserEntity(User user) {
        UserEntity ue = new UserEntity();
        ue.setUsername(user.getUsername());
        ue.setPassword(user.getPassword());
        ue.setEmail(user.getEmail());
        ue.setFirstName(user.getFirstName());
        ue.setLastName(user.getLastName());
        ue.setDisplayName(user.getDisplayName());
        return ue;
    }

    private User toUser(UserEntity ue) {
        User user = new User();
        user.setUsername(ue.getUsername());
        user.setPassword(ue.getPassword());
        user.setEmail(ue.getEmail());
        user.setFirstName(ue.getFirstName());
        user.setLastName(ue.getLastName());
        user.setDisplayName(ue.getDisplayName());
        return user;
    }
}
