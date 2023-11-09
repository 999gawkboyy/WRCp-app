package com.nasa.wrcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("/")

        Toast.makeText(this, "Hello World!", Toast.LENGTH_LONG).show()
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        val victims = mutableListOf<VictimData?>()


        fun showVictims(victims: List<VictimData?>) {
            for (i in victims) {
                val svBtn = SelectVictimButton(this, null)
                svBtn.text = "${i?.NAME}\n${i?.IP}"
                Log.e("DATA", "${i?.NAME}")

                linearLayout.addView(svBtn)
            }
        }

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                if (dataSnapShot.exists()) {
                    for (snapshot in dataSnapShot.children) {
                        val data = snapshot.getValue(VictimData::class.java)
                        victims.add(data)
                    }

                    showVictims(victims)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "Could not get data from DB")
            }
        })
    }
}