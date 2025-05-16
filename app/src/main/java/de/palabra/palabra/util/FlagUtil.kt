package de.palabra.palabra.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import de.palabra.palabra.R

object FlagsUtil {
    @SuppressLint("DiscouragedApi")
    fun getFlagResId(context: Context, code: String): Int {
        val resName = "flag_${code.lowercase()}"
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        if (resId == 0) {
            Log.w("FlagsUtils", "Flag for code '$code' not found, using fallback.")
            return R.drawable.flag__unknown
        }
        return resId
    }
}