package com.example.magic_link_authentication;

import com.example.magic_link_authentication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtValidate {

    @InjectMocks
    JwtService jwtService;

    @Test
    public void Generate_NullToken() {
        String tokenRandom = "adsadssssssssssssssssssssssssssqeeeeeee34e32eqwdsad23ewdsc23rwfr4fevfdc43refvr32842";
        String actualToken = jwtService.validateToken(tokenRandom);
        assertNull(actualToken);
    }
    @Test
    public void GenerateToken_EmptyValue() {
        String actualToken = jwtService.validateToken("");
        assertNull(actualToken);
    }
}
