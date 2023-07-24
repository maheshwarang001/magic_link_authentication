package com.example.magic_link_authentication.controller;


import com.example.magic_link_authentication.model.User;
import com.example.magic_link_authentication.service.*;
import com.example.magic_link_authentication.validation.EncodeDecodeService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.magic_link_authentication.validation.EmailPatter.EMAIL_PATTERN;
import static com.example.magic_link_authentication.validation.SpaceRemover.removeSpaces;



/**
 * CONTROLLER CONTAINS ALL THE ENDPOINTS
 **/


@RequestMapping("/zally")
@Controller
@Slf4j
public class AuthController {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Autowired
    EmailService emailService;

    @Autowired
    UserService mainService;


    @Autowired
    JwtService jwtService;

    @Autowired
    EncodeDecodeService encodeDecodeService;

    @Autowired
    TokenValidatorService tokenValidatorService;


    /**
     * LANDING PAGE
     **/
    @GetMapping("/v1")
    public String launchPage() {
        return "login.html";
    }

    /**
     * The `login` method is a handler for the post request mapped to "/v1/login".
     * Parameters:
     * (String): String inputEmail contains email address.
     * (HttpSession): session
     * Return Type:
     * (Boolean)
     * Behavior
     * Edge cases for the email address are been checked > Extra space || Regex pattern.
     * Token created [[SUBJECT: user email id] , [EXPIRY: 20 mins] )
     * A thread is initialized to prevent block of MAIN thread due to JAVAMAILSENDER
     * Improved the time efficiency by 60seconds
     **/
    @PostMapping("/v1/login")
    public ResponseEntity<Boolean> login(@RequestParam("email") String inputEmail, HttpSession session) {

        System.out.println(inputEmail);

         /**  Extra space remover **/
        String email = removeSpaces(inputEmail);
        StringBuilder str = new StringBuilder();
        str.append("http://localhost:8080/zally/v1/validate?token=");

        /**  Regex Validation **/
        if (email.length() == 0 || !EMAIL_PATTERN.matcher(email).matches()) {
            log.error(email);
            return ResponseEntity.badRequest().body(Boolean.FALSE);
        }

        log.info(email);


        try {

            String temptoken = mainService.generateToken(email);
            str.append(temptoken);

            String token = str.toString();

            /** Edge case Token is null **/
            if (token.charAt(token.length() - 1) == '=') {
                log.error("Token not generate");
                throw new RuntimeException();

            } else {
                session.setAttribute("magicLinkEmail", email);

            }


            /** A thread is initialized to prevent block of MAIN thread due to JAVAMAILSENDER **/
            Runnable emailTask = () -> emailService.sendVerificationEmail(email, token);
            executorService.submit(emailTask);

        } catch (Exception e) {
            log.error("Error during login process: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Boolean.FALSE);
        }

        return ResponseEntity.ok(true);

    }


    /**
     * The `validateLink` method is a handler for the get request mapped to "/zally/v1/validate".
     * Parameters:
     * (String): String token contains token.
     * (HttpSession): session
     * Return Type:
     * Redirects if successfull to the (/zally/v1/token-validated/checkuser?data=) || if failed to login page
     * Behavior
     * Token validation
     * if true, create a session for the user containing JWT token.
     * Parse jwt token > Get user's email id > create user if exist or send UUID
     * UUID is encoded using base64 > passed along with the url
     * String data is decoded to get users UUID. A query is made to check user exist in database
     *
     * if valid then home page is rendered or else login page
     **/

    @GetMapping("/v1/validate")
    public String validateLink(@RequestParam(name = "token") String token,
                               HttpSession session) {

        String userEmail = jwtService.validateToken(token);


        if (userEmail != null) {
            session.setAttribute("magicLinkToken", token);

            log.info("Token validation successful for user: {}", userEmail);

            UUID data = mainService.checkOrCreateUser(userEmail);
            byte[] encode = encodeDecodeService.encode(data);
            String str = new String(encode);
            log.info(str);

            return "redirect:/zally/v1/token-validated/checkuser?data=" + str;
        }

        log.error("Token validation failed for token: {}", token);
        return "redirect:/zally/v1";
    }



    /**
     * The `authenticateUser` method is a handler for the get request mapped to "/zally/v1/token-validated/checkuser?data".
     * Parameters:
     * (String): The String contains user UUID in Encoded format.
     * Return Type:
     * Redirects if successfull to the home page || if failed to login page
     * Behavior
     * Extra validation to check user->
     * String data is decoded to get users UUID. A query is made to check user exist in database
     * Edge case if user is null
     * if valid then home page is rendered or else login page
     **/



    @GetMapping("/v1/token-validated/checkuser")
    public String authenticateUser(@RequestParam(name = "data") String data) {

        byte[] sData = data.getBytes();
        UUID userId = encodeDecodeService.decode(sData, UUID.class);

        User user = mainService.getUser(userId);
        log.info("user iD :" + userId);


        if (user != null) {
            log.info("user iD :" + userId);
            return "redirect:/zally/v2/home";
        }

        log.error("user null reference");
        return "redirect:/zally/v1";


    }


    /**
     * The `homePage` method is a handler for the get request mapped to "/auth/v2/home".
     * Parameters:
     * (HttpSession): The HttpSession object representing the user session.
     * Return Type:
     * String: It returns view rendered on the server side.
     * Behavior
     * Token is retrieved from the session
     * performed asynchronous token validation to improve the responsiveness by not blocking the main thread for token validation.
     * if token is valid then home page is rendered or else login page
     **/


    @GetMapping("/v2/home")
    public String homePage(HttpSession session) {
        String session1 = (String) session.getAttribute("magicLinkToken");

        CompletableFuture<Boolean> validationFuture = tokenValidatorService.validateToken(session1);
        boolean isValidToken = validationFuture.join();

        if (!isValidToken) {
            return "redirect:/zally/v1";
        }

        return "home";
    }



 /**
 * The `logout` method is a handler for the post request mapped to "/auth/v2/home/logout". It is responsible for logout
 * Parameters:
 * (HttpSession): The HttpSession object representing the user session.
 * Return Type:
 * String: It returns a String representing the view name to be rendered on the server side.
 * Multi threading is used to remove JWT token from the session and by the time the main thread must've redirected to the login page endpoint
 **/

    @PostMapping("/v2/home/logout")
    public String logout(HttpSession session){

        Runnable logoutTask = () -> session.removeAttribute("magicLinkToken");
        executorService.submit(logoutTask);
        return "redirect:/zally/v1";

    }

}
