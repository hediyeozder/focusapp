package com.example.focusapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudyRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val minute: Int
)