package com.argus.argus_server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    void caseStatusIsOnlyOpenOrClosed() {
        VictimCase victimCase = new VictimCase();
        victimCase.updateStatus("closed");
        assertEquals("closed", victimCase.getStatus());

        assertThrows(IllegalArgumentException.class, () -> victimCase.updateStatus("banana"));
        assertThrows(IllegalArgumentException.class, () -> victimCase.updateStatus(null));
        assertEquals("closed", victimCase.getStatus());
    }

    @Test
    void survivorRefIsRequiredAndAtMost100Characters() {
        VictimCase victimCase = new VictimCase();
        victimCase.changeSurvivorRef("  SR-0042  ");
        assertEquals("SR-0042", victimCase.getSurvivorRef());

        assertThrows(IllegalArgumentException.class, () -> victimCase.changeSurvivorRef("   "));
        assertThrows(IllegalArgumentException.class, () -> victimCase.changeSurvivorRef("x".repeat(500)));
    }

    @Test
    void devicePlatformMustBeAndroidOrIos() {
        Device device = new Device();
        device.setPlatform("Android");
        device.validate();
        assertEquals("android", device.getPlatform());

        device.setPlatform("<script>alert(1)</script>");
        assertThrows(IllegalArgumentException.class, device::validate);

        device.setPlatform("ios");
        device.setModel("m".repeat(500));
        assertThrows(IllegalArgumentException.class, device::validate);
    }

    @Test
    void hugePasswordIsRejectedWithoutHashing() {
        AppUser user = new AppUser();
        user.setPassword("correct horse");
        assertFalse(user.login("a".repeat(100_000)));
    }
}
