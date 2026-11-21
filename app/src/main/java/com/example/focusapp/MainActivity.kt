package com.example.focusapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.focusapp.AppDatabase
import com.example.focusapp.StopwatchPage
import com.example.focusapp.StudyHistoryPage
import com.example.focusapp.TimerPage
import kotlinx.coroutines.launch
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private lateinit var totalFocus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val setTimer = findViewById<Button>(R.id.timer)
        val tracker = findViewById<Button>(R.id.tracker)
        totalFocus = findViewById(R.id.focusCoin)

        tracker.setOnClickListener {
            startActivity(Intent(this, StudyHistoryPage::class.java))
        }
        setTimer.setOnClickListener {
            showTimerDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@MainActivity)
            val total = db.studyDao().getTotalMinutes() ?: 0
            totalFocus.text = "Toplam: $total dk"
        }
    }

    private fun showTimerDialog() {
        val timerDialog = Dialog(this)
        val view = layoutInflater.inflate(R.layout.timer_dialog, null)
        timerDialog.setContentView(view)

        val start = view.findViewById<Button>(R.id.start)
        val timerTypeBtn = view.findViewById<Button>(R.id.timerType)
        val txtDisplay = view.findViewById<TextView>(R.id.txtMinuteDisplay)
        val pomoSeekBar = view.findViewById<SeekBar>(R.id.pomoSeekBar)
        var isCountdown = true
        var selectedMinute = 25

        timerTypeBtn.setOnClickListener {
            isCountdown = !isCountdown
            if (isCountdown) {
                timerTypeBtn.text = "countdown"
                txtDisplay.text = "25 : 00"
                pomoSeekBar.visibility = View.VISIBLE
            } else {
                timerTypeBtn.text = "stopwatch"
                txtDisplay.text = "00 : 00"
                pomoSeekBar.visibility = View.GONE
            }
        }

        pomoSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedMinute = (progress / 5) * 5 + 5
                txtDisplay.text = "$selectedMinute : 00"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        start.setOnClickListener {
            timerDialog.dismiss()
            if (isCountdown) {
                val intent = Intent(this, TimerPage::class.java)
                intent.putExtra("selectedMinute", selectedMinute)
                startActivity(intent)
            } else {
                startActivity(Intent(this, StopwatchPage::class.java))
            }
        }

        timerDialog.show()
    }
}