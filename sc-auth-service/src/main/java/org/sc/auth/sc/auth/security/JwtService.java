package org.sc.auth.sc.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.sc.auth.sc.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMS;

    public JwtService(
            @Value("${app.security.jwt.secret}") String base64Secret,
            @Value("${app.security.jwt.expiration-ms:900000}") long expirationMS
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMS = expirationMS;
    }

    // Generate Token
    public String generateToken(User user){

        Instant now  = Instant.now();

        List<String> roles = user.getRoles().stream().map(Enum::name).toList();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMS)))
                .signWith(signingKey)
                .compact();
    }

    public Claims verifyAndGetClaims(String token){
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Helper Methods
    public UUID getUserId(Claims claims){
        return UUID.fromString(claims.getSubject());
    }

    public List<String> getRoles(Claims claims){
        return claims.get("roles",List.class);
    }
}
