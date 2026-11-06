package com.argus_scanner

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScannerTest {
    @Test
    fun scanFindsThisApp() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val apps = scanDevice(ctx).getJSONArray("apps")
        val self = (0 until apps.length()).map { apps.getJSONObject(it) }
            .first { it.getString("package_name") == ctx.packageName }

        assertEquals(64, self.getString("cert_hash").length) // SHA-256 as hex
        assertFalse(self.getBoolean("hidden"))
    }
}