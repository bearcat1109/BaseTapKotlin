// StatsScreen.kt
package dev.bearcat.basetap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class PlayerStats(
    val playerName: String,
    val wins: Int,
    val losses: Int,
    val totalGames: Int,
    val winRate: Double
)

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    gameRepository: GameRepository
) {
    var playerStats by remember { mutableStateOf<List<PlayerStats>>(emptyList()) }
    var totalGamesPlayed by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var showClearConfirmation by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Load stats when screen opens
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val allResults = gameRepository.getAllGameResults()
                totalGamesPlayed = allResults.size

                // Calculate player statistics
                val playerStatsMap = mutableMapOf<String, Triple<Int, Int, Int>>() // wins, losses, total

                allResults.forEach { result ->
                    // Winner stats
                    val winnerStats = playerStatsMap[result.winnerName] ?: Triple(0, 0, 0)
                    playerStatsMap[result.winnerName] = Triple(
                        winnerStats.first + 1, // wins
                        winnerStats.second, // losses
                        winnerStats.third + 1 // total games
                    )

                    // Loser stats
                    val loserStats = playerStatsMap[result.loserName] ?: Triple(0, 0, 0)
                    playerStatsMap[result.loserName] = Triple(
                        loserStats.first, // wins
                        loserStats.second + 1, // losses
                        loserStats.third + 1 // total games
                    )
                }

                // Convert to PlayerStats list and sort by win rate
                playerStats = playerStatsMap.map { (name, stats) ->
                    val winRate = if (stats.third > 0) (stats.first.toDouble() / stats.third) * 100 else 0.0
                    PlayerStats(name, stats.first, stats.second, stats.third, winRate)
                }.sortedByDescending { it.winRate }

                isLoading = false
            } catch (e: Exception) {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Game Statistics",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = { showClearConfirmation = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear Stats",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total games summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "Total Games Played: $totalGamesPlayed",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (playerStats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No games played yet!\nPlay some games to see statistics here.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Player stats list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playerStats) { stats ->
                        PlayerStatsCard(stats = stats)
                    }
                }
            }
        }
    }

    // Clear confirmation dialog
    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("Clear All Statistics") },
            text = { Text("This will permanently delete all game records and statistics. This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            gameRepository.clearAllResults()
                            playerStats = emptyList()
                            totalGamesPlayed = 0
                            showClearConfirmation = false
                        }
                    }
                ) {
                    Text("Clear All", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PlayerStatsCard(stats: PlayerStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Player name
            Text(
                text = stats.playerName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Wins
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${stats.wins}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                    Text(
                        text = "Wins",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Losses
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${stats.losses}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(
                        text = "Losses",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Win Rate
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${String.format("%.1f", stats.winRate)}%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                    Text(
                        text = "Win Rate",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Total Games
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${stats.totalGames}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Games",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}