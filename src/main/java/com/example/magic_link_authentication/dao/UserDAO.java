package com.example.magic_link_authentication.dao;

import com.example.magic_link_authentication.model.User;
import com.example.magic_link_authentication.repository.UserDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.magic_link_authentication.validation.EmailPatter.EMAIL_PATTERN;


@Slf4j
@Repository
public class UserDAO implements UserDaoInterface {


    @Autowired
    UserDB userDB;

    @Override
    public User checkUserExist(String email) {

        /**MULTIPLE VALIDATION **/
        if (email.length() == 0 || !EMAIL_PATTERN.matcher(email).matches()) {
            log.error("EMAIL TYPE ERROR: " + email);
            return null;
        }

        Optional<User> user = userDB.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public void createUser(String email) {

        if(email.length() == 0 ||  !EMAIL_PATTERN.matcher(email).matches()){
            log.error("EMAIL TYPE ERROR", email);
            return ;
        }

        try {

            User user = new User();
            user.setEmail(email);
            user.setDoj(LocalDateTime.now());
            user.setLastLogin(LocalDateTime.now());

            userDB.save(user);
            log.info("User created :" + email);


        } catch (Exception e) {
            // Handle other exceptions
            log.error("Exception occurred:", e);

        }
    }

    @Override
    public UUID getUserUUID(String email) {
        Optional<User> user = userDB.findByEmail(email);

        return user.map(User::getUuid).orElse(null);
    }

    @Override
    public User userByID(UUID id) {
        Optional<User> user = userDB.findByUuid(id);
        return user.orElse(null);
    }

    public void saveLastLogin(String email){
        Optional<User> user = userDB.findByEmail(email);
        log.info("TIME UPDATED");
        user.ifPresent(value -> value.setLastLogin(LocalDateTime.now()));
    }


}
