package com.example.magic_link_authentication.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;


@Data
@Entity
@Table(name = "user_details")
public class User extends UserLogDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "username")
    private String email;

}
