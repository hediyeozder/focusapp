package com.example.focusapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FinishPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finish_page)

        val congratsText = findViewById<TextView>(R.id.congratsText)
        val studiedMinute = intent.getIntExtra("studiedMinute",0)
        congratsText.text = "Tebrikler $studiedMinute min çalıştın"

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@FinishPage)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            if (studiedMinute > 0) {
                db.studyDao().insert(StudyRecord(date = date, time = time, minute = studiedMinute))
            }
        }

        val homeBtn = findViewById<Button>(R.id.homeBtn)
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }
}