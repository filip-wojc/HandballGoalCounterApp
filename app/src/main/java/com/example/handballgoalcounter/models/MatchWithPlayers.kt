package com.example.handballgoalcounter.models

import androidx.room.Embedded
import androidx.room.Relation

data class MatchWithPlayers (
    @Embedded val match: Match,
    @Relation(
        parentColumn = "matchId",
        entityColumn = "matchId"
    )
    val players: List<Player>
)