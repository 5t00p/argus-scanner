package com.argus_scanner

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    private fun field(label: String, value: String, type: Int) = EditText(this).apply {
        hint = label
        setText(value)
        inputType = type
        setSingleLine()
        setTextColor(Color.WHITE)
        setHintTextColor(Color.parseColor("#9AA5B1"))
        backgroundTintList = ColorStateList.valueOf(Color.parseColor("#9AA5B1"))
        layoutParams = LinearLayout.LayoutParams(dp(280), LinearLayout.LayoutParams.WRAP_CONTENT)
            .apply { bottomMargin = dp(12) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 10.0.2.2 is the emulator's address for the PC; on a real phone use https://localhost:8080 with "adb reverse tcp:8080 tcp:8080"
        val serverInput = field("Server address", "https://10.0.2.2:8080",
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI)
        val codeInput = field("Pairing code (from the dashboard)", "", InputType.TYPE_CLASS_NUMBER)

        val output = TextView(this).apply {
            setTextColor(Color.parseColor("#E0E0E0"))
            gravity = Gravity.CENTER
            setPadding(0, dp(16), 0, 0)
        }

        val button = Button(this).apply {
            text = "Scan device"
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#00796B"))
            }
            layoutParams = LinearLayout.LayoutParams(dp(200), dp(200)).apply { topMargin = dp(12) }
        }

        button.setOnClickListener {
            val pairingCode = codeInput.text.toString().trim()
            if (!pairingCode.matches(Regex("\\d{6}"))) {
                output.text = "Enter the 6-digit pairing code from the dashboard"
                return@setOnClickListener
            }
            val server = serverInput.text.toString().trim()
            button.isEnabled = false
            output.text = "Scanning..."

            // network calls are not allowed on the UI thread, so scan and upload in the background
            thread {
                val scan = scanDevice(this)
                val file = File(getExternalFilesDir(null), "scan.json")
                file.writeText(scan.toString(2)) // kept as a backup for manual upload on the dashboard
                val apps = scan.getJSONArray("apps").length()

                val result = try {
                    "Scanned $apps apps\nUploaded: scan ID ${uploadScan(server, pairingCode, scan)}"
                } catch (e: Exception) {
                    "Scanned $apps apps\nUpload failed: ${e.message}\nSaved to ${file.absolutePath}"
                }
                runOnUiThread {
                    output.text = result
                    button.isEnabled = true
                }
            }
        }

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            fitsSystemWindows = true
            setPadding(dp(24), dp(24), dp(24), dp(24))
            setBackgroundColor(Color.parseColor("#1E2A38"))
            addView(serverInput)
            addView(codeInput)
            addView(button)
            addView(output)
        })
    }
}
