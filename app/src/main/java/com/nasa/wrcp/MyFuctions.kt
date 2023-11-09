package com.nasa.wrcp

import android.content.Context
import android.widget.Toast

class MyFuctions {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}