package com.example.focusapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudyRecordAdapter(private val records: List<StudyRecord>) :
    RecyclerView.Adapter<StudyRecordAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.time)
        val studyMinute: TextView = view.findViewById(R.id.studyMinute)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.time.text = "${record.time} :"
        holder.studyMinute.text = "${record.minute} dk"
    }

    override fun getItemCount() = records.size
}