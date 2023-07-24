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

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public interface TokenValidationCallback {
        void onTokenValidationResult(boolean isValidToken);
    }

    public CompletableFuture<Boolean> validateToken(String token) {
        return CompletableFuture.supplyAsync(() -> jwtService.validateToken(token) != null, executorService);
    }

    // Rest of the code remains the same...

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
