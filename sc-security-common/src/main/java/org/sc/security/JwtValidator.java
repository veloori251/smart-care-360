package org.sc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.UUID;

public class JwtValidator {

    private final SecretKey secretKey;

    public JwtValidator(String base64Secret){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    public Claims verifyAndGetClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getUserId(Claims claims){
        return UUID.fromString(claims.getSubject());
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims){
        List<String> roles = claims.get("roles", List.class);
        return roles!=null?roles:List.of();
    }
}
