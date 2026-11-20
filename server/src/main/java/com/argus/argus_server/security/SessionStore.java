package com.argus.argus_server.security;

import com.argus.argus_server.model.AppUser;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Logged-in sessions, stored in memory. If the server restarts everyone just logs in again. */
@Component
public class SessionStore {

    public record Session(Integer userID, String name, String role, Instant expiresAt) {
    }

    private static final Duration LIFETIME = Duration.ofHours(8);

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String create(AppUser user) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessions.put(token, new Session(user.getUserID(), user.getName(), user.getRole(), Instant.now().plus(LIFETIME)));
        return token;
    }

    public Optional<Session> find(String token) {
        if (token == null) {
            return Optional.empty();
        }
        Session session = sessions.get(token);
        if (session == null) {
            return Optional.empty();
        }
        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(session);
    }

    public void remove(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }

    /** Pulls the token out of an "Authorization: Bearer <token>" header. */
    public static String bearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring("Bearer ".length()).trim();
    }
}
