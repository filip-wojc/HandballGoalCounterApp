package com.example.handballgoalcounter.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.handballgoalcounter.models.Match
import com.example.handballgoalcounter.models.MatchWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Upsert
    suspend fun upsertMatch(match: Match)

    @Delete
    suspend fun deleteMatch(match: Match)

    @Query("SELECT * FROM `match` WHERE matchId = :id")
    fun getMatchById(id: Int) : Flow<MatchWithPlayers>

    @Query("SELECT * FROM `match`")
    fun getAllMatches() : Flow<List<Match>>

    @Query("UPDATE `match` SET teamAGoals = :teamAGoals, teamBGoals = :teamBGoals WHERE matchId = :matchId")
    suspend fun updateMatchGoals(matchId: Int, teamAGoals: Int, teamBGoals: Int)

}