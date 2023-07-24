package com.example.magic_link_authentication.service;


import com.example.magic_link_authentication.dao.UserDAO;
import com.example.magic_link_authentication.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService implements MainInterface {

    @Autowired
    JwtService jwtService;



    @Autowired
    UserDAO dao;


    /**
     * GENERIC METHOD TO SERVE MULTIPLE PURPOSES
     * **/
    @Override
    public <T> T checkEmail(String email, Class<T> targetType) {


        Object userExistResult = dao.checkUserExist(email);

        if (Boolean.class.equals(targetType)) {
            return targetType.cast(userExistResult != null);
        } else if (User.class.equals(targetType)) {
            return targetType.cast(userExistResult);
        }

        return null; // For other target types
    }


    /**
     * PASSES PARAM TO THE DAO TO CHECK USER EXIST
     * **/

    @Override
    public void Create(String email) {

        if(!checkEmail(email,boolean.class)){
            dao.createUser(email);
        }


    }


    /**
     * TOKEN GENERATE
     * **/
    @Override
    public String generateToken(String email) {

        String token = jwtService.generateToken(email);
        return !token.isEmpty()?token:"";

    }

    /**
     * GET USER OBJECT FROM THE DAO **/
    @Override
    public User getUser(UUID id) {
       return dao.userByID(id);

    }

    /**
     * GET UUID BY EMAIL, IF NOT EXIST THEN CREATE **/
    public UUID checkOrCreateUser(String email){
         Create(email);
        return dao.getUserUUID(email);

    }


}

