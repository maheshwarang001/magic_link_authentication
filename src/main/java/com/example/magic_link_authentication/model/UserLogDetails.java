package com.example.magic_link_authentication.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
/** LOG USER JOIN IN DATE AND LAST_LOG **/
@Data
@MappedSuperclass
public class UserLogDetails {


    @Column(name = "DOJ")
    private LocalDateTime doj;

    @Column(name = "last_log")
    private LocalDateTime lastLogin;
}
