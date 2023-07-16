package com.swipe.catalog.utils

import android.content.Context
import android.widget.Toast

class CustomToast(private val context: Context) {
    private var toast: Toast? = null

    fun show(message: String, duration: Int = Toast.LENGTH_SHORT) {
        toast?.cancel() // Cancel any previous toast if it's still showing
        toast = Toast.makeText(context, message, duration)
        toast?.show()
    }
}
