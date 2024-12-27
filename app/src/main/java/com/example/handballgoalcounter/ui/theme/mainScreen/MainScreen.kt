package com.example.handballgoalcounter.ui.theme.mainScreen

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.handballgoalcounter.R
import com.example.handballgoalcounter.models.Player

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel,
    navController: NavController,
) {
    val teamA = mainScreenViewModel.playersFromTeamA
    val teamB = mainScreenViewModel.playersFromTeamB
    val teamAGoals = mainScreenViewModel.teamAGoals.collectAsState()
    val teamBGoals = mainScreenViewModel.teamBGoals.collectAsState()
    val teamAName = mainScreenViewModel.teamAName.collectAsState()
    val teamBName = mainScreenViewModel.teamBName.collectAsState()
    val playerA = mainScreenViewModel.playerANumberInput.collectAsState()
    val playerB = mainScreenViewModel.playerBNumberInput.collectAsState()
    val isDeleting = mainScreenViewModel.isDeleting.collectAsState()


    Column(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                IconButton(
                    onClick = {navController.popBackStack()},
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground)
                }
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(0.5f), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = teamAName.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.widthIn(100.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.width(5.dp))
                Row(Modifier.weight(0.5f), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = teamBName.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.widthIn(100.dp),
                        textAlign = TextAlign.Center
                    )
                }

            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${teamAGoals.value}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text=":",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "${teamBGoals.value}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            // Left Column: Team A
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = playerA.value,
                    label = { Text("Gracz A") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = { mainScreenViewModel.onNumberAChange(it) },
                    modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.66f)
                )
                Button(
                    onClick = { mainScreenViewModel.addPlayerToTeamA(playerA.value) },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text("Dodaj")
                }
                // List of Team A players
                TeamList(
                    playerList = teamA,
                    addGoal = {player -> mainScreenViewModel.addGoalToPlayerA(player.playerId)},
                    removeGoal = {player -> mainScreenViewModel.removeGoalFromPlayerA(player.playerId)},
                    removePlayer = {player -> mainScreenViewModel.removePlayerA(player.playerId)},
                    isDeleting = isDeleting.value
                )
            }
            Spacer(Modifier.width(20.dp))
            // Right Column: Team B
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = playerB.value,
                    label = { Text("Gracz B") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    onValueChange = { mainScreenViewModel.onNumberBChange(it) },
                    modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.66f)
                )
                Button(
                    onClick = { mainScreenViewModel.addPlayerToTeamB(playerB.value) },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text("Dodaj")
                }
                // List of Team B players
                TeamList(
                    playerList = teamB,
                    addGoal = {player -> mainScreenViewModel.addGoalToPlayerB(player.playerId)},
                    removeGoal = {player -> mainScreenViewModel.removeGoalFromPlayerB(player.playerId)},
                    removePlayer = {player -> mainScreenViewModel.removePlayerB(player.playerId)},
                    isDeleting = isDeleting.value
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row (Modifier.padding(vertical = 10.dp)){
                Button(
                    onClick = {mainScreenViewModel.toogleDelete()}
                ) {
                    if (!isDeleting.value) {
                        Text("Usuń zawodników")
                    }
                    else {
                        Text("Przestań usuwać")
                    }
                }
            }
        }
    }

}

@Composable
fun TeamList(playerList: List<Player>,
             addGoal: (player: Player) -> Unit,
             removeGoal: (player: Player) -> Unit,
             removePlayer: (player: Player) -> Unit,
             isDeleting: Boolean
) {
    if (playerList.isNotEmpty()) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            val weight = if (isDeleting) 0.2f else 0.27f
            Icon(painter = painterResource(R.drawable.baseline_sports_handball_24), contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(weight)
            )
            Icon(painter = painterResource(R.drawable.baseline_sports_basketball_24), contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(weight)
            )
            Spacer(modifier = Modifier.weight(0.8f))
        }
    }
    Spacer(Modifier.height(2.dp))
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(15.dp)
        ).padding(horizontal = 8.dp)
    ){
        items(playerList) { player ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(player.number.toString(), modifier = Modifier.weight(0.48f), fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground)
                Text(player.goalCount.toString(), modifier = Modifier.weight(0.48f), fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground)
                IconButton(
                    onClick = {addGoal(player)},
                    modifier = Modifier.weight(0.52f)
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground)
                }
                IconButton(
                    onClick = {removeGoal(player)},
                    modifier = Modifier.weight(0.52f)
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground)
                }
                if (isDeleting) {
                    IconButton(
                        onClick = {removePlayer(player)},
                        modifier = Modifier.weight(0.52f)
                    ) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground)
                    }
                }

            }
        }
    }
}
