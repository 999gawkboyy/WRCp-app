package com.nasa.wrcp

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView

class ChatView(context: Context, attrs: AttributeSet?, user: String, chat: String): androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    init {
        if (user.equals("HACKER")) {
            this.setTextColor(Color.RED)
        }
        else {
            this.setTextColor(Color.BLACK)
        }
        this.text = chat
    }
}