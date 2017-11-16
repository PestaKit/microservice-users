package io.pestakit.users.api.endpoints;

import io.pestakit.users.api.UsersApi;
import io.pestakit.users.api.model.User;
import io.pestakit.users.entities.UserEntity;
import io.pestakit.users.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserApiController implements UsersApi {


    @Autowired
    UserRepository userRepository;


    @Override
    public ResponseEntity<Object> createUser(@ApiParam(value = "", required = true) @RequestBody User user) {

        for (UserEntity userEntity : userRepository.findAll()) {
            //if the ressource already exist
            if(userEntity.getUsername().compareToIgnoreCase(user.getUsername()) == 0 ||
                    userEntity.getEmail().compareToIgnoreCase(user.getEmail()) == 0){
                //can't create the object
                return ResponseEntity.status(500).build();

            }
        }

        //if there is no username, email or password we can't create object
        if(user.getUsername().isEmpty() ||
                user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        //we find all character which are not alphanumeric or - and _
        Pattern badPattern = Pattern.compile("[^a-zA-Z0-9] && [^-_]");
        Matcher matcher = badPattern.matcher(user.getUsername());
        boolean found = matcher.find();
        //if there is special character we can't create the object
        if(found){
            return ResponseEntity.status(500).build();
        }

        //for user of heig-vd (firstname.lastname@heig-vd)
        /*Pattern emailPattern = Pattern.compile("(.*)\\.(.*)@");
        matcher = emailPattern.matcher(user.getEmail());
        found = matcher.find();
        if(!found){
            return ResponseEntity.badRequest().build();
        }*/

        //if there is not @ character in email we can't create object
        if (!user.getEmail().contains("@")){
            return ResponseEntity.status(500).build();
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

    public ResponseEntity<User> getUser(long id) {
        UserEntity userEntity = userRepository.findOne(id);
        User user = toUser(userEntity);

        return ResponseEntity.ok(user);
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
