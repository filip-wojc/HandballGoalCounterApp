package com.example.handballgoalcounter.ui.theme.matchesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.handballgoalcounter.models.Match
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.ui.text.style.TextAlign
import com.example.handballgoalcounter.MatchScreen


@Composable
fun MatchesScreen(
    viewModel: MatchesScreenViewModel,
    navController: NavController
) {
    val matches = viewModel.matches
    val isDialogVisible = viewModel.isDialogVisible.collectAsState()
    val teamAName = viewModel.teamAName.collectAsState()
    val teamBName = viewModel.teamBName.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Mecze",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        LazyColumn (
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f).padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(matches) { match ->
                MatchCard(
                    match,
                    onDelete = {viewModel.deleteMatch(it.matchId)},
                    onClick = {
                        navController.navigate(MatchScreen(it.matchId))
                    }
                )
                Spacer(Modifier.height(15.dp))
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {viewModel.showResultDialog()}
            ) {
                Text("Dodaj mecz")
            }
        }

        if (isDialogVisible.value) {
            AlertDialog(
                onDismissRequest = { viewModel.hideResultDialog() },
                title = {
                    Text("Dodaj mecz")
                },
                text = {
                    Column (
                        Modifier.padding(10.dp).wrapContentWidth(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                       Column {
                           TextField(
                               value = teamAName.value,
                               label = { Text("Drużyna A") },
                               onValueChange = { viewModel.onTeamAChange(it) },
                               modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
                           )
                           Spacer(Modifier.width(8.dp))
                           TextField(
                               value = teamBName.value,
                               label = { Text("Drużyna B") },
                               onValueChange = { viewModel.onTeamBChange(it) },
                               modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
                           )
                       }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val match = Match(
                                matchId = 0,
                                teamAName = teamAName.value,
                                teamBName = teamBName.value,
                                teamAGoals = 0,
                                teamBGoals = 0
                            )
                            viewModel.addNewMatch(match)
                        },
                        enabled = teamAName.value != "" && teamBName.value != ""
                    ) {
                        Text("Dodaj")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { viewModel.hideResultDialog() }
                    ) {
                        Text("Anuluj")
                    }
                }
            )
        }
    }

}


@Composable
fun MatchCard(match: Match, onDelete: (match: Match) -> Unit, onClick: (match: Match) -> Unit) {
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(15.dp),
        ).fillMaxWidth(0.9f).padding(10.dp)
            .clickable { onClick(match) },
    ) {
        Column {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(Modifier.weight(0.5f), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = match.teamAName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
                Row{
                    IconButton(
                        onClick = { onDelete(match) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "",
                            modifier = Modifier.background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            ).padding(4.dp)
                        )
                    }
                }
                Row(Modifier.weight(0.5f), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = match.teamBName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                        )
                }

            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${match.teamAGoals}",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "${match.teamBGoals}",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${match.date.year}/${match.date.month.value}/${match.date.dayOfMonth}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

        }

    }
}