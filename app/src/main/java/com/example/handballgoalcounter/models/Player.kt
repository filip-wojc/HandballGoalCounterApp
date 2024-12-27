package com.example.handballgoalcounter.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Match::class,
            parentColumns = arrayOf("matchId"),
            childColumns = arrayOf("matchId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Player (
    @PrimaryKey(autoGenerate = true)
    val playerId: Int,
    val matchId: Int,
    val team: String,
    val number: Int,
    var goalCount: Int = 0
)