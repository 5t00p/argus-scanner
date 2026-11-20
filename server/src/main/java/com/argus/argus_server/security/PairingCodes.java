package com.argus.argus_server.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One-time 6-digit codes that link the scanner app to a device. We use these instead of logging in
 * on the survivor's phone since stalkerware on it could grab the advocate's password.
 * Stored in memory. Codes expire after 10 minutes anyway so a restart just means making a new one.
 */
@Component
public class PairingCodes {

    public static final Duration LIFETIME = Duration.ofMinutes(10);

    private record Pairing(int deviceId, Instant expiresAt) {
    }

    private final Map<String, Pairing> codes = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    private final Clock clock;

    public PairingCodes() {
        this(Clock.systemUTC());
    }

    PairingCodes(Clock clock) {
        this.clock = clock;
    }

    public String create(int deviceId) {
        codes.values().removeIf(pairing -> pairing.expiresAt().isBefore(clock.instant()));
        String code;
        do {
            code = String.format("%06d", random.nextInt(1_000_000));
        } while (codes.containsKey(code));
        codes.put(code, new Pairing(deviceId, clock.instant().plus(LIFETIME)));
        return code;
    }

    /** The device this code belongs to, if the code exists and hasn't expired. */
    public Optional<Integer> deviceFor(String code) {
        Pairing pairing = code == null ? null : codes.get(code);
        if (pairing == null || pairing.expiresAt().isBefore(clock.instant())) {
            return Optional.empty();
        }
        return Optional.of(pairing.deviceId());
    }

    /** A code works for one upload only. */
    public void use(String code) {
        codes.remove(code);
    }
}
