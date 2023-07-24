package com.example.magic_link_authentication.service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class TokenValidatorService {

    @Autowired
    JwtService jwtService;

    /**ExecutorService to handle token validation asynchronously**/
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    /** Method to validate a token asynchronously using CompletableFuture **/
    public CompletableFuture<Boolean> validateToken(String token) {
        /** CompletableFuture.supplyAsync() allows us to run the token validation in a separate thread **/
        /** The lambda function inside supplyAsync() returns true if the token is valid, otherwise false **/
        return CompletableFuture.supplyAsync(() -> jwtService.validateToken(token) != null, executorService);
    }

/** Method to clean up and shutdown the ExecutorService when the service is destroyed **/
    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
