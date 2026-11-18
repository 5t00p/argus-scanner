package com.argus_scanner

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/** Sends the scan to the server's upload endpoint and returns the new scan ID. Call off the UI thread. */
fun uploadScan(serverUrl: String, deviceId: Int, scan: JSONObject): Int {
    val connection = URL("${serverUrl.trimEnd('/')}/api/devices/$deviceId/scans").openConnection() as HttpURLConnection
    try {
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.connectTimeout = 5_000
        connection.readTimeout = 15_000
        connection.doOutput = true
        connection.outputStream.use { it.write(scan.toString().toByteArray()) }

        val code = connection.responseCode
        val body = (if (code < 400) connection.inputStream else connection.errorStream)
            ?.bufferedReader()?.use { it.readText() } ?: ""
        if (code !in 200..299) {
            throw IOException(if (code == 404) "device $deviceId not found on the server" else "server returned $code")
        }
        return JSONObject(body).getInt("scan_id")
    } finally {
        connection.disconnect()
    }
}
