package com.example.focusapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudyDao {
    @Insert
    suspend fun insert(record: StudyRecord)

    @Query("SELECT * FROM StudyRecord ORDER BY id DESC")
    suspend fun getAll(): List<StudyRecord>

    @Query("SELECT * FROM StudyRecord WHERE date = :date")
    suspend fun getByDate(date: String): List<StudyRecord>

    @Query("SELECT * FROM StudyRecord WHERE date LIKE :monthPrefix || '%'")
    suspend fun getByMonth(monthPrefix: String): List<StudyRecord>

    @Query("SELECT SUM(minute) FROM StudyRecord")
    suspend fun getTotalMinutes(): Int?


}