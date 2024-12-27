package com.example.handballgoalcounter.ui.theme.mainScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handballgoalcounter.DAO.MatchDao
import com.example.handballgoalcounter.DAO.PlayerDao
import com.example.handballgoalcounter.models.Match
import com.example.handballgoalcounter.models.MatchWithPlayers
import com.example.handballgoalcounter.models.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.jvm.internal.Ref.BooleanRef

class MainScreenViewModel(
    private val matchId: Int,
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao
) : ViewModel () {
    val placeholderMatch = Match(
        1, "", "", 0,0
    )

    private val _matchWithPlayers = MutableStateFlow<MatchWithPlayers>(
        MatchWithPlayers(placeholderMatch, listOf()),
        )

    private val _playersFromTeamA = mutableStateListOf<Player>()
    val playersFromTeamA: List<Player> = _playersFromTeamA

    private val _playersFromTeamB = mutableStateListOf<Player>()
    val playersFromTeamB: List<Player> = _playersFromTeamB

    private val _teamAGoals = MutableStateFlow<Int>(0)
    val teamAGoals: StateFlow<Int> = _teamAGoals

    private val _teamBGoals = MutableStateFlow<Int>(0)
    val teamBGoals: StateFlow<Int> = _teamBGoals

    private val _teamAName = MutableStateFlow<String>("")
    val teamAName: StateFlow<String> = _teamAName

    private val _teamBName = MutableStateFlow<String>("")
    val teamBName: StateFlow<String> = _teamBName

    private val _playerANumberInput = MutableStateFlow<String>("")
    val playerANumberInput: StateFlow<String> = _playerANumberInput

    private val _playerBNumberInput = MutableStateFlow<String>("")
    val playerBNumberInput: StateFlow<String> = _playerBNumberInput

    private val _isDeleting = MutableStateFlow<Boolean>(false)
    val isDeleting : StateFlow<Boolean> = _isDeleting

    init {
        loadMatchData(matchId)
    }

    fun addPlayerToTeamA(playerNumber: String) {
        if (playerNumber.isNotEmpty() && _playersFromTeamA.count() < 16) {
            val player = Player(0, matchId = matchId,"A", playerNumber.toInt())
            viewModelScope.launch {
                playerDao.addPlayer(player)
                _playersFromTeamA += player
            }
            _playerANumberInput.value = ""
        }

    }

    fun addPlayerToTeamB(playerNumber: String) {
        if (playerNumber.isNotEmpty() && _playersFromTeamB.count() < 16) {
            val player = Player(0,matchId,"B", playerNumber.toInt())
            viewModelScope.launch {
                playerDao.addPlayer(player)
                _playersFromTeamB += player
            }
            _playerBNumberInput.value = ""
        }
    }


    fun addGoalToPlayerA(playerId: Int) {
        val player = _playersFromTeamA.find { it.playerId == playerId }
        if (player != null) {
            viewModelScope.launch {
                playerDao.updatePlayerGoals(playerId, player.goalCount + 1)
                val index = _playersFromTeamA.indexOf(player)
                _playersFromTeamA[index] = player.copy(goalCount = player.goalCount + 1)
                _teamAGoals.value += 1
                matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
            }
        } else {
            println("Gracz o ID $playerId nie istnieje w drużynie A")
        }
    }

    fun addGoalToPlayerB(playerId: Int) {
        val player = _playersFromTeamB.find { it.playerId == playerId }
        if (player != null) {
            viewModelScope.launch {
                playerDao.updatePlayerGoals(playerId, player.goalCount + 1)
                val index = _playersFromTeamB.indexOf(player)
                _playersFromTeamB[index] = player.copy(goalCount = player.goalCount + 1)
                _teamBGoals.value += 1
                matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
            }

        } else {
            println("Gracz o ID $playerId nie istnieje w drużynie B")
        }
    }

    fun removeGoalFromPlayerA(playerId: Int) {
        val player = _playersFromTeamA.find { it.playerId == playerId }
        if (player != null) {
            if (_teamAGoals.value > 0 && player.goalCount > 0) {
                viewModelScope.launch {
                    playerDao.updatePlayerGoals(playerId, player.goalCount - 1)
                    val index = _playersFromTeamA.indexOf(player)
                    _playersFromTeamA[index] = player.copy(goalCount = player.goalCount - 1)
                    _teamAGoals.value -= 1
                    matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
                }
            }
        } else {
            println("Gracz o ID $playerId nie istnieje w drużynie B")
        }
    }

    fun removeGoalFromPlayerB(playerId: Int) {
        val player = _playersFromTeamB.find { it.playerId == playerId }
        if (player != null) {
            if (_teamBGoals.value > 0 && player.goalCount > 0) {
                viewModelScope.launch {
                    playerDao.updatePlayerGoals(playerId, player.goalCount - 1)
                    val index = _playersFromTeamB.indexOf(player)
                    _playersFromTeamB[index] = player.copy(goalCount = player.goalCount - 1)
                    _teamBGoals.value -= 1
                    matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
                }
            }
        } else {
            println("Gracz o ID $playerId nie istnieje w drużynie B")
        }
    }

    fun onNumberAChange(number: String) {
        if (number.length <= 2) {
            _playerANumberInput.value = number.filter { it.isDigit() }
        }
    }

    fun onNumberBChange(number: String) {
        if (number.length <= 2) {
            _playerBNumberInput.value = number.filter { it.isDigit() }
        }
    }

    fun removePlayerA(playerId: Int) {
        val player = _playersFromTeamA.find { it.playerId == playerId }
        if (player != null) {
            viewModelScope.launch {
                playerDao.deletePlayer(player)
                _teamAGoals.value -= player.goalCount
                matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
                _playersFromTeamA.remove(player)
            }
        }
    }

    fun removePlayerB(playerId: Int) {
        val player = _playersFromTeamB.find { it.playerId == playerId }
        if (player != null) {
            viewModelScope.launch {
                playerDao.deletePlayer(player)
                _teamBGoals.value -= player.goalCount
                matchDao.updateMatchGoals(matchId, _teamAGoals.value, _teamBGoals.value)
                _playersFromTeamB.remove(player)
            }

        }
    }

    fun toogleDelete() {
        _isDeleting.value = !_isDeleting.value
    }

    fun loadMatchData(matchId: Int) {
        viewModelScope.launch {
            matchDao.getMatchById(matchId).collect {
                if (it != null) {
                    _matchWithPlayers.value = it

                    val newPlayersA = it.players.filter { it.team == "A" }
                    val newPlayersB = it.players.filter { it.team == "B" }

                    if (_playersFromTeamA != newPlayersA) {
                        _playersFromTeamA.clear()
                        _playersFromTeamA.addAll(newPlayersA)
                    }

                    if (_playersFromTeamB != newPlayersB) {
                        _playersFromTeamB.clear()
                        _playersFromTeamB.addAll(newPlayersB)
                    }

                    _teamAGoals.value = it.match.teamAGoals
                    _teamBGoals.value = it.match.teamBGoals
                    _teamAName.value = it.match.teamAName
                    _teamBName.value = it.match.teamBName

                }
            }
        }
    }
}