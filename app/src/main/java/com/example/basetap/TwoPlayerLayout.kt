package dev.bearcat.basetap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context

@Composable
fun TwoPlayerLayout(
    onShowPlayerCount: () -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit,
    gameRepository: GameRepository
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    }

    // Load saved values or use defaults
    var topLife by remember { mutableStateOf(0) }
    var bottomLife by remember { mutableStateOf(0) }

    var topImage by remember {
        mutableStateOf(
            sharedPreferences.getInt("top_leader_image", R.drawable.darth_maul_sith_revealed_showcase)
        )
    }
    var bottomImage by remember {
        mutableStateOf(
            sharedPreferences.getInt("bottom_leader_image", R.drawable.qui_gon_jinn_student_of_the_living_force_showcase)
        )
    }

    var topBaseId by remember {
        mutableStateOf(
            sharedPreferences.getInt("top_base_id", 47)
        )
    }
    var bottomBaseId by remember {
        mutableStateOf(
            sharedPreferences.getInt("bottom_base_id", 28)
        )
    }

    var playerNames by remember {
        mutableStateOf(
            listOf(
                sharedPreferences.getString("player_1_name", "Player 1") ?: "Player 1",
                sharedPreferences.getString("player_2_name", "Player 2") ?: "Player 2"
            )
        )
    }

    // Info button
    var showInfoDialog by remember { mutableStateOf(false) }

    // Victory dialog state
    var showVictoryDialog by remember { mutableStateOf(false) }
    var gameStartTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Helper function to save preferences
    fun savePreferences() {
        with(sharedPreferences.edit()) {
            putInt("top_leader_image", topImage)
            putInt("bottom_leader_image", bottomImage)
            putInt("top_base_id", topBaseId)
            putInt("bottom_base_id", bottomBaseId)
            putString("player_1_name", playerNames[0])
            putString("player_2_name", playerNames[1])
            apply()
        }
    }

    // Helper function to get leader name from image resource
    fun getLeaderNameFromImage(imageResource: Int): String {
        return try {
            context.resources.getResourceEntryName(imageResource)
        } catch (e: Exception) {
            "unknown_leader"
        }
    }

    // Check for victory condition (damage reaches base max health)
    LaunchedEffect(topLife, bottomLife, topBaseId, bottomBaseId) {
        // Get max health for each base using your existing Bases enum
        val topMaxHealth = Bases.findById(topBaseId)?.maxHealth ?: 30
        val bottomMaxHealth = Bases.findById(bottomBaseId)?.maxHealth ?: 30

        if (topLife >= topMaxHealth || bottomLife >= bottomMaxHealth) {
            if (!showVictoryDialog) {
                showVictoryDialog = true
            }
        }
    }

    // Reset function
    fun resetGame() {
        topLife = 0
        bottomLife = 0
        gameStartTime = System.currentTimeMillis()
        showVictoryDialog = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Player
        TwoPlayerCounter(
            modifier = Modifier
                .weight(0.475f)
                .fillMaxWidth(),
            isTopPlayer = true,
            backgroundImageId = topImage,
            baseId = topBaseId,
            life = topLife,
            playerName = playerNames[0],
            onNameChange = { newName ->
                playerNames = playerNames.toMutableList().also { it[0] = newName }
                savePreferences()
            },
            onImageChange = {
                topImage = it
                savePreferences()
            },
            onBaseChange = {
                topBaseId = it
                savePreferences()
            },
            onLifeChange = { topLife = it },
            initiativePlayer = initiativePlayer,
            onInitiativeClaimed = onInitiativeClaimed,
            playerId = 0
        )

        // Middle Controls
        Row(
            modifier = Modifier
                .weight(0.05f)
                .fillMaxWidth()
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        resetGame()
                    }
            )

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Select Player Count",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onShowPlayerCount()
                    }
            )

            Text(
                text = "Blast",
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        if (topLife >= 0) topLife += 1
                        if (bottomLife >= 0) bottomLife += 1
                    }
            )

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable{
                        showInfoDialog = true
                    }
            )
        }

        // Bottom Player
        TwoPlayerCounter(
            modifier = Modifier
                .weight(0.475f)
                .fillMaxWidth(),
            isTopPlayer = false,
            backgroundImageId = bottomImage,
            baseId = bottomBaseId,
            life = bottomLife,
            playerName = playerNames[1],
            onNameChange = { newName ->
                playerNames = playerNames.toMutableList().also { it[1] = newName }
                savePreferences()
            },
            onImageChange = {
                bottomImage = it
                savePreferences()
            },
            onBaseChange = {
                bottomBaseId = it
                savePreferences()
            },
            onLifeChange = { bottomLife = it },
            initiativePlayer = initiativePlayer,
            onInitiativeClaimed = onInitiativeClaimed,
            playerId = 1
        )
    }

    // Victory Dialog with Database Storage
    if (showVictoryDialog) {
        VictoryDialog(
            player1Name = playerNames[0],
            player2Name = playerNames[1],
            player1Life = topLife,
            player2Life = bottomLife,
            player1BaseId = topBaseId,
            player2BaseId = bottomBaseId,
            player1LeaderImage = topImage,
            player2LeaderImage = bottomImage,
            onWinnerSelected = { winnerIndex ->
                val gameDuration = (System.currentTimeMillis() - gameStartTime) / 60000 // Convert to minutes

                CoroutineScope(Dispatchers.IO).launch {
                    if (winnerIndex == 0) {
                        // Player 1 (top) wins
                        gameRepository.saveGameResult(
                            winnerName = playerNames[0],
                            winnerBaseId = topBaseId,
                            winnerLeaderImage = getLeaderNameFromImage(topImage),
                            winnerLife = topLife,
                            loserName = playerNames[1],
                            loserBaseId = bottomBaseId,
                            loserLeaderImage = getLeaderNameFromImage(bottomImage),
                            loserLife = bottomLife,
                            gameDurationMinutes = gameDuration
                        )
                    } else {
                        // Player 2 (bottom) wins
                        gameRepository.saveGameResult(
                            winnerName = playerNames[1],
                            winnerBaseId = bottomBaseId,
                            winnerLeaderImage = getLeaderNameFromImage(bottomImage),
                            winnerLife = bottomLife,
                            loserName = playerNames[0],
                            loserBaseId = topBaseId,
                            loserLeaderImage = getLeaderNameFromImage(topImage),
                            loserLife = topLife,
                            gameDurationMinutes = gameDuration
                        )
                    }
                }
                resetGame()
            },
            onDismiss = {
                showVictoryDialog = false
            }
        )
    }

    // Info Dialog
    if(showInfoDialog){
        InfoDialog(
            onDismissRequest = { showInfoDialog = false }
        )
    }
}