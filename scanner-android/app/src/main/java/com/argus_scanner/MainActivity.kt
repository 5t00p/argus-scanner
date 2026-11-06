package com.argus_scanner

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val output = TextView(this)
        val size = (200 * resources.displayMetrics.density).toInt() // 200dp in pixels
        val button = Button(this).apply {
            text = "Scan device"
            setTextColor(Color.WHITE)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor("#00796B"))   // button color
            }
            layoutParams = LinearLayout.LayoutParams(size, size)
        }

        button.setOnClickListener {
            // shortcut: scans on the UI thread, move to a background thread if it freezes on phones with many apps
            val scan = scanDevice(this)
            val file = File(getExternalFilesDir(null), "scan.json")
            file.writeText(scan.toString(2))
            output.text = "Scanned ${scan.getJSONArray("apps").length()} apps\nSaved to ${file.absolutePath}"
        }

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            fitsSystemWindows = true
            setPadding(48, 48, 48, 48)
            addView(button)
            addView(output)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1E2A38"))   // background color
        })
    }
}