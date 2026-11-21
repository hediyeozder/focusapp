package com.example.focusapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val records: List<DayItem>,
    private val onDayClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayNumber: TextView = view.findViewById(R.id.dayNumber)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_box, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = records[position]
        if (day.date.isEmpty()) {
            holder.dayNumber.text = ""
            holder.dayNumber.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.dayNumber.text = day.date.takeLast(2)
            holder.dayNumber.setBackgroundColor(getColorForMinutes(day.totalMinutes))
            holder.dayNumber.setOnClickListener {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())
                if (day.date == today || (day.date < today && day.totalMinutes > 0)) {
                    onDayClick(day.date)
                }
            }
        }
    }

    override fun getItemCount() = records.size
    private fun getColorForMinutes(minutes: Int): Int {
        return when {
            minutes == 0 -> Color.TRANSPARENT
            minutes in 1..25 -> Color.parseColor("#9FE1CB")
            minutes in 26..50 -> Color.parseColor("#5DCAA5")
            minutes in 51..90 -> Color.parseColor("#1D9E75")
            else -> Color.parseColor("#0F6E56")
        }
    }

}