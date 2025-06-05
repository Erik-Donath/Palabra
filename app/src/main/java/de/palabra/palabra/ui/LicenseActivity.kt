package de.palabra.palabra.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import de.palabra.palabra.R

class LicenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)

        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }
    }
}
