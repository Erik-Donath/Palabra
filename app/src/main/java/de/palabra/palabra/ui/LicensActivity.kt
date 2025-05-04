package de.palabra.palabra.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import de.palabra.palabra.R

class LicensActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licens)

        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }
    }
}
