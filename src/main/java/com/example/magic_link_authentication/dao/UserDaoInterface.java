package com.example.magic_link_authentication.dao;

import com.example.magic_link_authentication.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDaoInterface {

    User checkUserExist(String email);


    void createUser(String email);


    UUID getUserUUID(String email);

    User userByID(UUID id);
}
