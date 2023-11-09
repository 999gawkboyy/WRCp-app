package com.nasa.wrcp

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActivityChooserView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.time.LocalDateTime

class Chatting : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)
        val mf = MyFuctions()
        val intent = intent
        var NAME = intent.getStringExtra("NAME")
        val linearLY = findViewById<LinearLayout>(R.id.linearLayout2)
        val inputChat = findViewById<EditText>(R.id.inputChat)
        val sendBtn = findViewById<ImageButton>(R.id.sendBtn)
        val chatScrollView = findViewById<ScrollView>(R.id.chatscroll)
        val lp = chatScrollView.layoutParams
        val resources: Resources = resources

        val dbRefHacker: DatabaseReference = FirebaseDatabase.getInstance().reference.child("/${NAME}/CHAT/HACKER")

        val dbRefVictim: DatabaseReference = FirebaseDatabase.getInstance().reference.child("/${NAME}/CHAT/VICTIM")

        inputChat.onFocusChangeListener = View.OnFocusChangeListener{_, hasFocus ->
            if (!hasFocus) {
                val dp = 600
                val pxValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
                ).toInt()
                lp.height = pxValue
                chatScrollView.layoutParams = lp
            }
            else {
                val dp = 250
                val pxValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
                ).toInt()
                lp.height = pxValue
                chatScrollView.layoutParams = lp
            }
        }

        sendBtn.setOnClickListener{
            val chat = inputChat.text.toString()
            if (!chat.equals("")) {
                dbRefHacker.setValue(chat + "/HACKER/" + LocalDateTime.now())
                    .addOnCompleteListener {    task ->
                        if (task .isSuccessful) {

                        }
                        else {
                            mf.showToast(this, "FAILED!!! SOMETHING ERROR")
                        }
                    }
            }
            inputChat.text = null
        }

        fun showChatHacker(value: String) {
            val user = value.split("/")[1]
            val chat = value.split("/")[0]
            val chatView = ChatView(this, null, user, chat)

            linearLY.addView(chatView)
        }

        fun showChatVictim(value: String) {
            val user = value.split("/")[1]
            val chat = value.split("/")[0]
            val chatView = ChatView(this, null, user, chat)

            linearLY.addView(chatView)
        }

        val valueEventListenerHacker = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터가 변경될 때 호출됩니다.
                val value = dataSnapshot.value.toString()
                Log.e("a",value)
                showChatHacker(value)
                chatScrollView.post {
                    chatScrollView.fullScroll(View.FOCUS_DOWN)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("데이터 변경 감지 오류: ${error.message}")
            }
        }

        val valueEventListenerVictim = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터가 변경될 때 호출됩니다.
                val value = dataSnapshot.value.toString()
                Log.e("a",value)
                showChatVictim(value)
                chatScrollView.post {
                    chatScrollView.fullScroll(View.FOCUS_DOWN)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("데이터 변경 감지 오류: ${error.message}")
            }
        }

// ValueEventListener를 Firebase 데이터베이스에 연결
        dbRefHacker.addValueEventListener(valueEventListenerHacker)
        dbRefVictim.addValueEventListener(valueEventListenerVictim)
    }
}
