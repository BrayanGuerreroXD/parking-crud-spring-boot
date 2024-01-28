package com.api.parking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    private final static Long accessTokenSeconds = 6 * 60 * 60L;

    public String createToken(String email, String role, Long id){
        Duration duration = Duration.ofSeconds(accessTokenSeconds);
        Date expirationDate = new Date(System.currentTimeMillis() + duration.toMillis());

        Map<String, Object> extra = new HashMap<>();
        extra.put("role", role);
        extra.put("id", id);

        return Jwts.builder().setSubject(email).setExpiration(expirationDate).addClaims(extra)
            .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes())).compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token){
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecretKey.getBytes()).build()
                .parseClaimsJws(token).getBody();
            String email= claims.getSubject();
            Collection<SimpleGrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        }catch (JwtException e){
            return null;
        }
    }
}