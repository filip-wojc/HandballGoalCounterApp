package com.example.handballgoalcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.handballgoalcounter.DAO.MatchDatabase
import com.example.handballgoalcounter.models.Player
import com.example.handballgoalcounter.tools.MainScreenViewModelFactory
import com.example.handballgoalcounter.ui.theme.AppTheme
import com.example.handballgoalcounter.ui.theme.mainScreen.MainScreen
import com.example.handballgoalcounter.ui.theme.mainScreen.MainScreenViewModel
import com.example.handballgoalcounter.ui.theme.matchesScreen.MatchesScreen
import com.example.handballgoalcounter.ui.theme.matchesScreen.MatchesScreenViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = Room.databaseBuilder(
                applicationContext,
                MatchDatabase::class.java,
                "match_db"
            ).build()
            val matchDao = database.matchDao
            val playerDao = database.playerDao
            val matchesScreenViewModel = MatchesScreenViewModel(matchDao)
            AppTheme(darkTheme = true) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Matches
                ) {
                    composable<MatchScreen> {
                        val args = it.arguments?.getInt("matchId") ?: 0
                        val mainScreenViewModel: MainScreenViewModel = viewModel(
                            factory = MainScreenViewModelFactory(args, matchDao, playerDao)
                        )
                        MainScreen(
                            mainScreenViewModel = mainScreenViewModel,
                            navController = navController,
                        )
                    }

                    composable<Matches> {
                        MatchesScreen(
                            viewModel = matchesScreenViewModel,
                            navController = navController,
                        )
                    }
                }
            }

        }
    }
}

@Serializable
data class MatchScreen(
    val matchId: Int
)

@Serializable
object Matches