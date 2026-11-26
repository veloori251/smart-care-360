package org.sc.auth.sc.auth.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sc.auth.sc.auth.dto.AuthResponse;
import org.sc.auth.sc.auth.dto.LoginRequest;
import org.sc.auth.sc.auth.dto.RegisterRequest;
import org.sc.auth.sc.auth.dto.UserProfileResponse;
import org.sc.auth.sc.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){

        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // 3. Protected: Verify Jwt filter works (Required valid token)
    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication){
        UserProfileResponse profile = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(profile);
    }
}
