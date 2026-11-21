package com.example.focusapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TimerPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer_page)

        val selectedMinute = intent.getIntExtra("selectedMinute", 25)
        val countdownTimer = findViewById<TextView>(R.id.countdownTimer)
        countdownTimer.text = "$selectedMinute : 00"

        val finishbtn = findViewById<Button>(R.id.finishbtn)
        val timerBack = findViewById<Button>(R.id.timerBack)
        val stopstartbtn = findViewById<Button>(R.id.stopstartbtn)

        val totalMillis = selectedMinute * 60 * 1000L
        var currentMillis = totalMillis
        var isTimer = true
        var activeTimer: CountDownTimer? = null

        fun startTimer() {
            activeTimer = object : CountDownTimer(currentMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    currentMillis = millisUntilFinished
                    val minute = millisUntilFinished / 1000 / 60
                    val second = millisUntilFinished / 1000 % 60
                    countdownTimer.text = "$minute : ${String.format("%02d", second)}"
                }
                override fun onFinish() {
                    countdownTimer.text = "0 : 00"
                    val intent = Intent(this@TimerPage, FinishPage::class.java)
                    intent.putExtra("studiedMinute", selectedMinute)
                    startActivity(intent)
                }
            }.start()
        }

        startTimer()

        timerBack.setOnClickListener {
            activeTimer?.cancel()
            finish()
        }

        stopstartbtn.setOnClickListener {
            isTimer = !isTimer
            if (isTimer) {
                startTimer()
                stopstartbtn.text = "stop"
            } else {
                activeTimer?.cancel()
                stopstartbtn.text = "start"
            }
        }

        finishbtn.setOnClickListener {
            activeTimer?.cancel()
            val studiedSeconds = (totalMillis - currentMillis) / 1000
            val studiedMinute = (studiedSeconds / 60).toInt()
            val intent = Intent(this, FinishPage::class.java)
            intent.putExtra("studiedMinute", studiedMinute)
            startActivity(intent)
        }
    }
}