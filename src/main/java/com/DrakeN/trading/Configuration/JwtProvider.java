package com.DrakeN.trading.Configuration;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRECT_KEY.getBytes());

    public static String generateToken(Authentication subject) {
        Collection<? extends GrantedAuthority> authorities = subject.getAuthorities();
        String roles = populateAuthorities(authorities);
        String jwt = Jwts.builder().issuedAt(new Date()).expiration(new Date(new Date().getTime() + 8640000))
                .claim("email", subject.getName()).claim("authorities", roles).signWith(key).compact();
        return jwt;

    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            auth.add(ga.getAuthority());

        }
        return String.join(",", auth);
    }

    public static String getEmailFromToken(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String email = String.valueOf(claims.get("email"));
        return email;

    }

}
