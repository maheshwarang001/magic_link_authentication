package com.example.magic_link_authentication.service;


import com.example.magic_link_authentication.dao.UserDAO;
import com.example.magic_link_authentication.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class UserService implements UserInterface {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);


    @Autowired
    JwtService jwtService;


    @Autowired
    UserDAO dao;


    /**
     * GENERIC METHOD TO SERVE MULTIPLE PURPOSES
     **/
    @Override
    public <T> T checkEmail(String email, Class<T> targetType) {
        Object userExistResult = dao.checkUserExist(email);

        if (Boolean.class.equals(targetType)) {
            return targetType.cast(userExistResult != null);
        } else if (User.class.equals(targetType)) {

            // Check if the result is not null and is an instance of User
            if (userExistResult != null && userExistResult instanceof User) {
                return targetType.cast(userExistResult);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * TOKEN GENERATE
     **/
    @Override
    public String generateToken(String email) {
        /** EDGE CASE IN THE CONTROLLER FOR 0 LENGTH TOKEN **/

        if (email.isEmpty()) return "";
        String token = jwtService.generateToken(email);
        return Optional.ofNullable(token).orElse("");

    }

    /**
     * GET USER OBJECT FROM THE DAO
     **/
    @Override
    public User getUser(UUID id) {
        return dao.userByID(id);

    }

    /**
     * GET UUID BY EMAIL, IF NOT EXIST THEN CREATE
     **/
    public UUID checkOrCreateUser(String email) {
        log.info("UUID :" + email);

        if (!checkEmail(email, Boolean.class)) {
            /** Email does not exist, create the user **/
            try {
                dao.createUser(email);
            } catch (Exception e) {
                /** EDGE CASES **/
                return null;
            }
        }

        /**UPDATE LAST LOGIN IN DIFFERENT THREAD**/
        Runnable updatetime = () ->  dao.saveLastLogin(email);
        executorService.submit(updatetime);

        return dao.getUserUUID(email);

    }


}

