package com.example.magic_link_authentication;

import com.example.magic_link_authentication.dao.UserDAO;
import com.example.magic_link_authentication.model.User;
import com.example.magic_link_authentication.service.JwtService;
import com.example.magic_link_authentication.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MagicLinkAuthenticationApplicationTests {

    @Mock
    private UserDAO dao;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void contextLoads() {

    }
    @Test
    public void testGetUser_NonExistingUser() {
        UUID nonExistingUserId = UUID.randomUUID();
        when(dao.userByID(nonExistingUserId)).thenReturn(null);
        User actualUser = userService.getUser(nonExistingUserId);
        assertNull(actualUser);
    }

    @Test
    public void testCheckOrCreateUser_ExistingUser() {
        String existingEmail = "iuybmm@76gn.com";
        UUID expectedUserId = UUID.randomUUID();
        when(dao.getUserUUID(existingEmail)).thenReturn(expectedUserId);
        when(dao.checkUserExist(existingEmail)).thenReturn(new User()); /** Mock the checkUserExist method**/
        UUID actualUserId = userService.checkOrCreateUser(existingEmail);
        assertNotNull(actualUserId);
        assertEquals(expectedUserId, actualUserId);
    }
    @Test
    public void testGenerateToken_NullToken() {
       String email = "test@example.com";
       when(jwtService.generateToken(email)).thenReturn(null);
        String actualToken = userService.generateToken(email);
        assertEquals("", actualToken);
    }
    @Test
    public void GenerateTest_ForEmptyString() {
        String email = "";
        when(jwtService.generateToken(email)).thenReturn(null);
        String actualToken = userService.generateToken(email);
        assertEquals("", actualToken);
    }

}
