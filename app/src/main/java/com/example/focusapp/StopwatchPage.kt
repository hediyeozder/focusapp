package com.example.focusapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class StopwatchPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stopwatch_page)
        val stopwatchTimer = findViewById<TextView>(R.id.stopwatchTimer)
        val finishbtn = findViewById<Button>(R.id.finishbtn)
        val stopstartbtn = findViewById<Button>(R.id.stopstartbtn)
        var seconds = 0
        var isRunning = true
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                seconds++
                val minute = seconds / 60
                val second = seconds % 60
                stopwatchTimer.text = "$minute : ${String.format("%02d", second)}"
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

        stopstartbtn.setOnClickListener {
            isRunning = !isRunning
            if (isRunning){ handler.post(runnable)
                stopstartbtn.text = "stop"}
            else{
                handler.removeCallbacks(runnable)
                stopstartbtn.text = "start"
            }
        }
        finishbtn.setOnClickListener {
            val studiedMinute = seconds / 60
            val intent = Intent(this, FinishPage::class.java)
            intent.putExtra("studiedMinute", studiedMinute)
            startActivity(intent)
        }
    }
}