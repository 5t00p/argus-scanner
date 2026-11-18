package com.argus.argus_server.controller;

import com.argus.argus_server.model.AppUser;
import com.argus.argus_server.repository.AppUserRepository;
import com.argus.argus_server.security.SessionStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String token, Integer userID, String name, String role) {
    }

    private final AppUserRepository users;
    private final SessionStore sessions;

    public AuthController(AppUserRepository users, SessionStore sessions) {
        this.users = users;
        this.sessions = sessions;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<AppUser> user = request.email() == null ? Optional.empty() : users.findByEmail(request.email().trim());
        // same message for unknown email and wrong password, so nobody can probe which emails exist
        if (user.isEmpty() || !user.get().login(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Wrong email or password"));
        }
        AppUser found = user.get();
        return ResponseEntity.ok(new LoginResponse(sessions.create(found), found.getUserID(), found.getName(), found.getRole()));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        sessions.remove(SessionStore.bearerToken(authorization));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return sessions.find(SessionStore.bearerToken(authorization))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "login required")));
    }
}
