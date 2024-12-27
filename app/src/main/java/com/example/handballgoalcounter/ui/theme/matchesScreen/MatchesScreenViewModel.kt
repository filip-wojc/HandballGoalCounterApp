package com.example.handballgoalcounter.ui.theme.matchesScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handballgoalcounter.DAO.MatchDao
import com.example.handballgoalcounter.models.Match
import com.example.handballgoalcounter.models.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MatchesScreenViewModel(
    private val dao: MatchDao
) : ViewModel() {

    private val _matches = mutableStateListOf<Match>()
    val matches: List<Match> = _matches

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible


    private val _teamAName = MutableStateFlow("")
    val teamAName: StateFlow<String> = _teamAName

    private val _teamBName = MutableStateFlow("")
    val teamBName: StateFlow<String> = _teamBName

    init {
        getAllMatches()
    }

    fun showResultDialog() {
        _isDialogVisible.value = true
    }

    fun hideResultDialog() {
        _isDialogVisible.value = false
        _teamAName.value = ""
        _teamBName.value = ""
    }

    fun onTeamAChange(name: String) {
        if (name.length <= 40) {
            _teamAName.value = name
        }
    }

    fun onTeamBChange(name: String) {
        if (name.length <= 40) {
            _teamBName.value = name
        }
    }

    fun addNewMatch(match: Match) {
        viewModelScope.launch {
            dao.upsertMatch(match)
            _matches.add(match)
            _teamAName.value = ""
            _teamBName.value = ""
        }
    }

    fun deleteMatch(matchId: Int) {
        viewModelScope.launch {
            val match = _matches.find { it.matchId == matchId }
            if (match != null) {
                _matches.remove(match)
                dao.deleteMatch(match)
            }
        }
    }

    fun getAllMatches() {
        viewModelScope.launch {
            dao.getAllMatches().collect { matches ->
                _matches.clear()
                _matches.addAll(matches)
            }
        }
    }
}