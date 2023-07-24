package com.example.magic_link_authentication.service;


import com.example.magic_link_authentication.roles.Roles;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/** THIS CLASS CREATES AND VALIDATES TOKEN**/
@Component
@Slf4j
public class JwtService {

    //128bit key
    @Value("${secret.key}")
    protected String SECRET;



    public String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName);
    }


    /**
     * SUBJECT: EMAIL ID
     * EXPIRY: 20MINS
     * **/
    private String createToken(Map<String,Object> claims,
                               String userName){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .claim("role", Roles.USER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 20))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey(){

        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /** RETURN USERS EMAIL ID PARSED FROM THE JWT TOKEN**/
    public String validateToken(final String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();

            // Check token expiration
            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
             if(!currentDate.after(expirationDate)){
                 return claims.getSubject();
             }
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token Invalid");
            return null;
        }

        return null;

    }

}
