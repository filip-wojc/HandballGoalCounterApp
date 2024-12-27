package com.example.handballgoalcounter.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.handballgoalcounter.DAO.MatchDao
import com.example.handballgoalcounter.DAO.PlayerDao
import com.example.handballgoalcounter.ui.theme.mainScreen.MainScreenViewModel

class MainScreenViewModelFactory(
    private val matchId: Int,
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            return MainScreenViewModel(matchId, matchDao, playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}