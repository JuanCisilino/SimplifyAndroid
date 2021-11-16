package com.example.pokereloaded.extentions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.hideKeyboard(view: View) {
    val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}
