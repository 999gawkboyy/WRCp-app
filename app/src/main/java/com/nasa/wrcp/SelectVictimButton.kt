package com.nasa.wrcp


import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class SelectVictimButton(context: Context, attrs: AttributeSet?) : AppCompatButton(context, attrs){
    init {
        setOnClickListener {
            // 사용자 정의 클릭 동작을 여기에 추가
            val intent = Intent(context, AttackVictim::class.java)
            val tmp = this.text.split("\n")
            val NAME = tmp[0]
            val IP = tmp[1]
            intent.putExtra("NAME", NAME)
            intent.putExtra("IP", IP)
            context.startActivity(intent)
        }
    }
}