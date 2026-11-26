package org.sc.auth.sc.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.sc.auth.sc.auth.dto.AuthResponse;
import org.sc.auth.sc.auth.dto.LoginRequest;
import org.sc.auth.sc.auth.dto.RegisterRequest;
import org.sc.auth.sc.auth.dto.UserProfileResponse;
import org.sc.auth.sc.auth.entity.Role;
import org.sc.auth.sc.auth.entity.User;
import org.sc.auth.sc.auth.repository.UserRepository;
import org.sc.auth.sc.auth.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.security.jwt.expiration-ms:900000}")
    private long expirationMs;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest){
        String normalizedEmail = registerRequest.email().trim().toLowerCase();

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new IllegalArgumentException("Email is already registered: "+normalizedEmail);
        }
        User user = User.builder()
                .email(normalizedEmail)
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(Set.of(Role.ROLE_CUSTOMER))
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        return AuthResponse.bearer(token,expirationMs);
    }

    @Transactional(readOnly=true)
    public AuthResponse login(LoginRequest request){
        String normalizedEmail = request.email().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail,request.password())
        );
        User user = userRepository.findByEmail(normalizedEmail).orElseThrow(
                () -> new IllegalStateException("User not found in our records"));
        String token = jwtService.generateToken(user);
        return AuthResponse.bearer(token,expirationMs);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUser(String userId){
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<String> roles = user.getRoles().stream()
                .map(Enum::name).toList();
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                roles,
                user.isEnabled()
        );
    }
}
