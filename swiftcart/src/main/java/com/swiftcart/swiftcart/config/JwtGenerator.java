package com.swiftcart.swiftcart.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JwtGenerator {
    public String generateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .claim("username", authentication.getName())
                .claim("authorities", populate(authentication.getAuthorities()))
                .issuedAt(new Date()).expiration(new Date(new Date().getTime() + 3_60_00000)).issuer("Swift-Cart").subject("Token for Using App").signWith(secretKey).compact();
        return jwt;
    }

    public String populate(Collection<? extends GrantedAuthority> authorities) {
        Set<String> set = new HashSet<>();
        for (GrantedAuthority g : authorities) {
            set.add(g.getAuthority());
        }
        return String.join(",", set);
    }

    public String getEmailFromToken(String jwt) {
        try {
            jwt = jwt.substring(7);
            Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(JwtConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8))).build().parseSignedClaims(jwt).getPayload();
            return String.valueOf(claims.get("username"));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
