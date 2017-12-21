package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.UsersApi;
import io.pestakit.users.api.model.DisplayUser;
import io.pestakit.users.api.model.NewUser;
import io.pestakit.users.api.model.User;
import io.pestakit.users.entities.UserEntity;
import io.pestakit.users.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.UUID;

@Controller
public class UserApiController implements UsersApi {


    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<Void> createUser(@ApiParam(value = "", required = true) @Valid @RequestBody User user) {
        //it is not up to the user to choose the id
        user.setId(null);
        //if the ressource already exist
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (userEntity != null) {
            //can't create the object
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //if all condition is plain we can now create the object
        UserEntity newUserEntity = toUserEntity(user);
        userRepository.save(newUserEntity);

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
    @Secured("IS_AUTHENTICATED")
    @PreAuthorize("hasPermission(#id, 'OWNER')")
    public ResponseEntity<User> updateUser(@ApiParam(value = "user's id", required = true)
                                           @PathVariable("id") String id, @Valid @RequestBody NewUser body) {
        UserEntity userEntity = userRepository.findById(UUID.fromString(id));
        if (userEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (body.getDisplayName() != null && !body.getDisplayName().isEmpty()) {
            userEntity.setDisplayName(body.getDisplayName());
        }
        if (body.getEmail() != null && !body.getEmail().isEmpty()) {
            userEntity.setEmail(body.getEmail());
        }
        if (body.getFirstName() != null && !body.getFirstName().isEmpty()) {
            userEntity.setFirstName(body.getFirstName());
        }
        if (body.getLastName() != null && !body.getLastName().isEmpty()) {
            userEntity.setLastName(body.getLastName());
        }
        if (body.getPassword() != null && !body.getPassword().isEmpty()) {
            userEntity.setPassword(body.getPassword());
        }
        userRepository.save(userEntity);
        User user = toUser(userEntity);

        return ResponseEntity.ok(user);
    }

//     public ResponseEntity<User> getUser(@ApiParam(value = "user's id",required=true )
//                                             @PathVariable("id") Long id) {
//        UserEntity userEntity = userRepository.findOne(id);
//        if (userEntity == null) {
//            return ResponseEntity.status(404).build();
//        }
//        User user = toUser(userEntity);
//
//        return ResponseEntity.ok(user);
//    }

    @Override
    public ResponseEntity<User> getUser(@ApiParam(value = "user's id",required=true ) @PathVariable("id") String id){
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(id);
        if (userEntity != null) {
            return ResponseEntity.ok(toUser(userEntity));
        }

        try {
            userEntity = userRepository.findById(UUID.fromString(id));
            if (userEntity != null) {
                return ResponseEntity.ok(toUser(userEntity));
            }
        } catch (IllegalArgumentException e){
            //nothing to do here
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
        ue.setIsAdmin(user.getIsAdmin());
        return ue;
    }

    private User toUser(UserEntity ue) {
        User user = new User();

        user.setId(ue.getId());
        user.setUsername(ue.getUsername());
        user.setPassword(ue.getPassword());
        user.setEmail(ue.getEmail());
        user.setFirstName(ue.getFirstName());
        user.setLastName(ue.getLastName());
        user.setDisplayName(ue.getDisplayName());
        return user;
    }

    private DisplayUser toDispayUser(User user) {
        DisplayUser displayUser = new DisplayUser();
        displayUser.setUsername(user.getUsername());
        displayUser.setDisplayName(user.getDisplayName());
        displayUser.setEmail(user.getEmail());
        displayUser.setFirstName(user.getFirstName());
        displayUser.setLastName(user.getLastName());
        return displayUser;
    }
}
