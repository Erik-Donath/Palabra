package de.palabra.palabra.util

import android.content.Intent
import android.os.Build

inline fun <reified T : java.io.Serializable> Intent.getSerializableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= 33) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }
}