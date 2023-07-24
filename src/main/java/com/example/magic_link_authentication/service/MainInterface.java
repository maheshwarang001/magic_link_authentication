package com.example.magic_link_authentication.service;

import com.example.magic_link_authentication.model.User;

import java.util.UUID;

public interface MainInterface {

    <T>T checkEmail(String email,Class<T> targetType );

    void Create(String email);

//    boolean CreateUser(String email);

    String generateToken(String email);

    User getUser(UUID id);


}
