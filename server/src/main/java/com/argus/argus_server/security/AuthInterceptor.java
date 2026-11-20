package com.argus.argus_server.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/** Checks every /api request for a valid session token. */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_ATTRIBUTE = "argusSession";
   // a real scan (~150 apps on my phone) is way under 1 MB so anything over 5 MB gets rejected
    private static final long MAX_BODY_BYTES = 5L * 1024 * 1024;

    private final SessionStore sessions;
    private final boolean enabled;

    public AuthInterceptor(SessionStore sessions, @Value("${argus.auth.enabled:true}") boolean enabled) {
        this.sessions = sessions;
        this.enabled = enabled;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getContentLengthLong() > MAX_BODY_BYTES) {
            response.setStatus(413);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"request is too large\"}");
            return false;
        }
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
        // the scanner app has no login: it proves itself with a one-time pairing code from the dashboard instead
        return "POST".equals(request.getMethod()) && path.matches("/api/pairing/\\d{6}/scans");
    }
}
