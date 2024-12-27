package com.example.handballgoalcounter.models

import android.icu.text.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Match (
    @PrimaryKey(autoGenerate = true)
    val matchId: Int,
    val teamAName: String,
    val teamBName: String,
    val teamAGoals: Int,
    val teamBGoals: Int,
    val date: LocalDateTime = LocalDateTime.now()
)