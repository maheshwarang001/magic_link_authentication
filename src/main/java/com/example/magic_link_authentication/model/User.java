package com.example.magic_link_authentication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;


import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**ENTITY CLASS**/
@Data
@Entity
@Table(name = "user_details")
public class User extends UserLogDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "username")
    @NotBlank()
    private String email;

}
