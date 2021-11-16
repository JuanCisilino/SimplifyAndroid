package com.example.pokereloaded.extentions

import android.view.View
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard(view: View){
    activity?.hideKeyboard(view)
}