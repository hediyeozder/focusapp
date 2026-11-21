package com.example.focusapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyHistoryPage : AppCompatActivity() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedDate: TextView
    private lateinit var monthTitle: TextView
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_study_history_page)

        val prevMonth = findViewById<Button>(R.id.prevMonth)
        val nextMonth = findViewById<Button>(R.id.nextMonth)
        monthTitle = findViewById(R.id.monthTitle)
        recyclerView = findViewById(R.id.studyRecyclerView)
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        selectedDate = findViewById(R.id.selectedDate)

        recyclerView.layoutManager = LinearLayoutManager(this)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        lifecycleScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date())
            selectedDate.text = "Today"
            val db = AppDatabase.getInstance(this@StudyHistoryPage)
            val records = db.studyDao().getByDate(today)
            recyclerView.adapter = StudyRecordAdapter(records)
        }

        loadMonth(currentYear, currentMonth)

        prevMonth.setOnClickListener {
            currentMonth--
            if (currentMonth == 0) {
                currentMonth = 12
                currentYear--
            }
            loadMonth(currentYear, currentMonth)
        }

        nextMonth.setOnClickListener {
            currentMonth++
            if (currentMonth == 13) {
                currentMonth = 1
                currentYear++
            }
            loadMonth(currentYear, currentMonth)
        }
    }

    fun loadMonth(year: Int, month: Int) {
        val monthNames = listOf("", "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
            "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık")
        monthTitle.text = "${monthNames[month]} $year"

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@StudyHistoryPage)
            val monthPrefix = String.format("%04d-%02d", year, month)
            val records = db.studyDao().getByMonth(monthPrefix)
            val grouped = records.groupBy { it.date }
            val dailyMinutes = grouped.mapValues { it.value.sumOf { record -> record.minute } }

            val daysInMonth = Calendar.getInstance().apply {
                set(year, month - 1, 1)
            }.getActualMaximum(Calendar.DAY_OF_MONTH)

            val firstDayOfWeek = Calendar.getInstance().apply {
                set(year, month - 1, 1)
            }.get(Calendar.DAY_OF_WEEK)


            val dayItems = mutableListOf<DayItem>()

            val emptyDays = (firstDayOfWeek + 5) % 7
            repeat(emptyDays) {
                dayItems.add(DayItem(date = "", totalMinutes = 0))
            }

            for (day in 1..daysInMonth) {
                val date = String.format("%04d-%02d-%02d", year, month, day)
                val minutes = dailyMinutes[date] ?: 0
                dayItems.add(DayItem(date = date, totalMinutes = minutes))
            }
            calendarRecyclerView.adapter = CalendarAdapter(dayItems) { date ->
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())
                selectedDate.text = if (date == today) "Today" else date
                lifecycleScope.launch {
                    val db = AppDatabase.getInstance(this@StudyHistoryPage)
                    val records = db.studyDao().getByDate(date)
                    recyclerView.adapter = StudyRecordAdapter(records)
                }
            }
        }
    }
}