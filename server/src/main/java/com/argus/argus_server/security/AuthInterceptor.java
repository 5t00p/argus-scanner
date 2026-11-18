package com.argus.argus_server.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/** Runs before every /api request: no valid session token, no data. */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_ATTRIBUTE = "argusSession";

    private final SessionStore sessions;
    private final boolean enabled;

    public AuthInterceptor(SessionStore sessions, @Value("${argus.auth.enabled:true}") boolean enabled) {
        this.sessions = sessions;
        this.enabled = enabled;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!enabled || isPublic(request)) {
            return true;
        }
        var session = sessions.find(SessionStore.bearerToken(request.getHeader("Authorization")));
        if (session.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"login required\"}");
            return false;
        }
        request.setAttribute(SESSION_ATTRIBUTE, session.get());
        return true;
    }

    private static boolean isPublic(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.equals("/api/login")) {
            return true;
        }
        // shortcut: the phone scanner has no login yet, so scan uploads stay open; replace with a one-time pairing code
        return "POST".equals(request.getMethod()) && path.matches("/api/devices/\\d+/scans");
    }
}
