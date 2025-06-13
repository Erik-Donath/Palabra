package de.palabra.palabra.util

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

inline fun <reified T : View> Activity.applyEdgeToEdgeInsets(viewId: Int) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val view = findViewById<T>(viewId)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}