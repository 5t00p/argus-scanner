package com.argus_scanner

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID

fun scanDevice(context: Context): JSONObject {
    val pm = context.packageManager
    val accessibility = Settings.Secure.getString(
        context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: ""
    val admins = context.getSystemService(DevicePolicyManager::class.java)
        .activeAdmins.orEmpty().map { it.packageName }.toSet()

    val apps = JSONArray()
    val flags = PackageManager.GET_PERMISSIONS or PackageManager.GET_SIGNING_CERTIFICATES
    for (pi in pm.getInstalledPackages(flags)) {
        val ai = pi.applicationInfo ?: continue
        // shortcut: skips preinstalled system apps, include them if stalkerware is found posing as a system app
        if (ai.flags and ApplicationInfo.FLAG_SYSTEM != 0) continue
        apps.put(
            JSONObject()
                .put("package_name", pi.packageName)
                .put("app_name", pm.getApplicationLabel(ai).toString())
                .put("cert_hash", certHash(pi))
                .put("hidden", pm.getLaunchIntentForPackage(pi.packageName) == null)
                .put("accessibility_enabled", accessibility.contains("${pi.packageName}/"))
                .put("device_admin", pi.packageName in admins)
                .put("permissions", JSONArray(grantedPermissions(pi)))
        )
    }

    return JSONObject()
        // lets the server ignore a retried upload of a scan it already saved
        .put("scan_uuid", UUID.randomUUID().toString())
        .put("platform", "android")
        .put("model", "${Build.MANUFACTURER} ${Build.MODEL}")
        .put("os_version", Build.VERSION.RELEASE)
        .put("scanned_at", Instant.now().toString())
        .put("apps", apps)
}

private fun certHash(pi: PackageInfo): String? {
    val cert = pi.signingInfo?.apkContentsSigners?.firstOrNull() ?: return null
    return MessageDigest.getInstance("SHA-256").digest(cert.toByteArray())
        .joinToString("") { "%02X".format(it) }
}

private fun grantedPermissions(pi: PackageInfo): List<String> {
    val perms = pi.requestedPermissions ?: return emptyList()
    val flags = pi.requestedPermissionsFlags ?: return emptyList()
    return perms.filterIndexed { i, _ -> flags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0 }
}