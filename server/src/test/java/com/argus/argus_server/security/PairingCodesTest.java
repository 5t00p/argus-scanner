package com.argus.argus_server.security;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class PairingCodesTest {

    /** A clock the test can move forward. */
    private static class TestClock extends Clock {
        Instant now = Instant.parse("2026-11-19T12:00:00Z");
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId zone) { return this; }
        public Instant instant() { return now; }
    }

    @Test
    void codeIsSixDigitsAndPointsToItsDevice() {
        PairingCodes codes = new PairingCodes();
        String code = codes.create(7);

        assertTrue(code.matches("\\d{6}"));
        assertEquals(7, codes.deviceFor(code).orElseThrow());
        assertTrue(codes.deviceFor("000000".equals(code) ? "111111" : "000000").isEmpty());
        assertTrue(codes.deviceFor(null).isEmpty());
    }

    @Test
    void codeWorksOnlyOnce() {
        PairingCodes codes = new PairingCodes();
        String code = codes.create(7);

        codes.use(code);

        assertTrue(codes.deviceFor(code).isEmpty());
    }

    @Test
    void codeExpiresAfterTenMinutes() {
        TestClock clock = new TestClock();
        PairingCodes codes = new PairingCodes(clock);
        String code = codes.create(7);

        clock.now = clock.now.plusSeconds(9 * 60);
        assertTrue(codes.deviceFor(code).isPresent());

        clock.now = clock.now.plusSeconds(2 * 60);
        assertTrue(codes.deviceFor(code).isEmpty());
    }
}
