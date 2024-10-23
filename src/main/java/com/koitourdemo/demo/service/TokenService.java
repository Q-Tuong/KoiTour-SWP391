package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {

    @Autowired
    UserRepository userRepository;

    public final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407trumpet";

    private Set<String> blacklistedTokens = new HashSet<>();

    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        String token = Jwts.builder()
                .subject(user.getId()+"")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    public User getUserByToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String idString = claims.getSubject();
        long id = Long.parseLong(idString);
        return userRepository.findUserById(id);
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenValid(String token) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
