package com.example.handballgoalcounter.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.handballgoalcounter.models.MatchWithPlayers
import com.example.handballgoalcounter.models.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Upsert
    suspend fun addPlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)

    @Query("UPDATE `player` SET goalCount = :goals WHERE playerId = :playerId")
    suspend fun updatePlayerGoals(playerId: Int, goals: Int)
}