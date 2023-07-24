package com.example.magic_link_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class MagicLinkAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagicLinkAuthenticationApplication.class, args);
    }

}
