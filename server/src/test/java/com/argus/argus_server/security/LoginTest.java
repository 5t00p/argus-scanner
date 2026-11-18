package com.argus.argus_server.security;

import com.argus.argus_server.model.AppUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void passwordIsHashedAndChecked() {
        AppUser user = new AppUser();
        user.setPassword("correct horse");

        assertFalse(user.getPasswordHash().contains("correct horse"));
        assertTrue(user.login("correct horse"));
        assertFalse(user.login("wrong password"));
        assertFalse(user.login(null));
    }

    @Test
    void samePasswordGetsDifferentHashes() {
        // a random salt per user means two equal passwords don't look equal in the database
        assertNotEquals(PasswordHasher.hash("password1"), PasswordHasher.hash("password1"));
    }

    @Test
    void userWithoutPasswordCannotLogIn() {
        assertFalse(new AppUser().login("anything"));
    }

    @Test
    void sessionTokenFindsUserUntilLogout() {
        SessionStore sessions = new SessionStore();
        AppUser user = new AppUser();
        user.setUserID(4);
        user.setRole("advocate");

        String token = sessions.create(user);

        assertEquals(4, sessions.find(token).orElseThrow().userID());
        assertTrue(sessions.find("made-up-token").isEmpty());
        sessions.remove(token);
        assertTrue(sessions.find(token).isEmpty());
    }

    @Test
    void bearerHeaderIsParsed() {
        assertEquals("abc", SessionStore.bearerToken("Bearer abc"));
        assertNull(SessionStore.bearerToken("Basic abc"));
        assertNull(SessionStore.bearerToken(null));
    }
}
