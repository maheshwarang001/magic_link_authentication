package com.example.magic_link_authentication.repository;

import com.example.magic_link_authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDB  extends JpaRepository<User,UUID > {

    Optional<User> findByEmail(String email);


    Optional<User> findByUuid(UUID id);





}
