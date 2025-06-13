package de.palabra.palabra.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import de.palabra.palabra.R
import de.palabra.palabra.util.applyEdgeToEdgeInsets

class LicenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        applyEdgeToEdgeInsets<View>(R.id.license)

        findViewById<Button>(R.id.backBtn).setOnClickListener { finish() }
    }
}
