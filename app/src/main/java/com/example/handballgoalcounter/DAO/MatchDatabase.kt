package com.example.handballgoalcounter.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.handballgoalcounter.models.Match
import com.example.handballgoalcounter.models.Player
import com.example.handballgoalcounter.tools.Converters

@Database(
    entities = [Match::class, Player::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MatchDatabase : RoomDatabase() {
    abstract val matchDao: MatchDao
    abstract val playerDao : PlayerDao
}