package com.argus.argus_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.argus.argus_server.model.AppUser;
import com.argus.argus_server.repository.AppUserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.argus.argus_server.repository.CaseAssignmentRepository;
import com.argus.argus_server.repository.AuditlogRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import com.argus.argus_server.security.AuthInterceptor;
import com.argus.argus_server.security.SessionStore;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AppUserRepository userRepository;
    private final CaseAssignmentRepository caseAssignmentRepository;
    private final AuditlogRepository auditlogRepository;

    public UserController(AppUserRepository userRepository, CaseAssignmentRepository caseAssignmentRepository, AuditlogRepository auditlogRepository) {
        this.userRepository = userRepository;
        this.caseAssignmentRepository = caseAssignmentRepository;
        this.auditlogRepository = auditlogRepository;
    }

    @GetMapping
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public record UserCreateRequest(String name, String email, String role, String password) {}

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request, HttpServletRequest http) {
        if (http.getAttribute(AuthInterceptor.SESSION_ATTRIBUTE) instanceof SessionStore.Session session
                && !"admin".equals(session.role())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Only admins can add users"));
        }
        if (request.email() == null || request.password() == null
                || request.password().length() < 8 || request.password().length() > 100) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email required and a minimum password length of 8"));
        }
        if (!request.email().trim().matches("[^@\\s]+@[^@\\s]+") || request.email().trim().length() > 100) {
            return ResponseEntity.badRequest().body(Map.of("error", "Enter a valid email address"));
        }
        if (userRepository.findByEmail(request.email().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "That email is already in use"));
        }
        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email().trim());
        user.setRole(request.role() == null ? "advocate" : request.role());
        user.setPassword(request.password());
        AppUser saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserUpdateResponse(saved.getUserID(), saved.getName(), saved.getEmail(), saved.getRole()));
    }

    public record UserUpdateRequest(String name, String email, String role) {}
    public record UserUpdateResponse(Integer userID, String name, String email, String role) {}

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequest updatedUser) {
        AppUser existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedUser.name() != null) {
            existingUser.setName(updatedUser.name());
        }

        if (updatedUser.email() != null) {
            existingUser.setEmail(updatedUser.email());
        }

        if (updatedUser.role() != null) {
            existingUser.setRole(updatedUser.role());
        }

        AppUser savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(new UserUpdateResponse(savedUser.getUserID(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (caseAssignmentRepository.existsByUserID(id)) {
            return ResponseEntity.status(409).build();
        }

        if (auditlogRepository.existsByUserID(id)) {
            return ResponseEntity.status(409).build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}