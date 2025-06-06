package dev.bearcat.basetap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FourPlayerLayout(
    onShowPlayerCount: () -> Unit = {},
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit
) {
    // Life totals for each square
    var topLeftLife by remember { mutableStateOf(0) }
    var topRightLife by remember { mutableStateOf(0) }
    var bottomLeftLife by remember { mutableStateOf(0) }
    var bottomRightLife by remember { mutableStateOf(0) }

    // Image resources for each square
    var topLeftImage by remember { mutableStateOf(R.drawable.boba_fett_any_methods_necessary_showcase) }
    var topRightImage by remember { mutableStateOf(R.drawable.han_solo_never_tell_me_the_odds_showcase) }
    var bottomLeftImage by remember { mutableStateOf(R.drawable.poe_dameron_i_can_fly_anything_showcase) }
    var bottomRightImage by remember { mutableStateOf(R.drawable.lando_calrissian_buying_time_showcase) }

    // Base IDs for each square
    var topLeftBaseId by remember { mutableStateOf(1) }
    var topRightBaseId by remember { mutableStateOf(1) }
    var bottomLeftBaseId by remember { mutableStateOf(1) }
    var bottomRightBaseId by remember { mutableStateOf(1) }

    // To track player names
    var playerNames by remember { mutableStateOf(List(4) { "Player ${it + 1}" }) }

    // State declaration for the player count menu
    var showPlayerCountDialog by remember { mutableStateOf(false) }
    // Info menu
    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Row
        Row(
            modifier = Modifier
                .weight(0.475f)
                .fillMaxWidth()
        ) {
            // Top Left
            LifeCounter(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isRightSide = false,
                backgroundImageId = topLeftImage,
                baseId = topLeftBaseId,
                life = topLeftLife,
                playerName = playerNames[0],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[0] = newName }
                },
                onImageChange = { topLeftImage = it },
                onBaseChange = { topLeftBaseId = it },
                onLifeChange = { topLeftLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 0
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Black)
            )

            // Top Right
            LifeCounter(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isRightSide = true,
                backgroundImageId = topRightImage,
                baseId = topRightBaseId,
                life = topRightLife,
                playerName = playerNames[1],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[1] = newName }
                },
                onImageChange = { topRightImage = it },
                onBaseChange = { topRightBaseId = it },
                onLifeChange = { topRightLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 1
            )
        }

        // Middle Section (5%)
        Row(
            modifier = Modifier
                .weight(0.05f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),  // Add some padding on the sides
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        topLeftLife = 0
                        topRightLife = 0
                        bottomLeftLife = 0
                        bottomRightLife = 0
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Player Count Button
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { onShowPlayerCount() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Select Player Count",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Blast Button
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        if (topLeftLife >= 0) topLeftLife += 1
                        if (topRightLife >= 0) topRightLife += 1
                        if (bottomLeftLife >= 0) bottomLeftLife += 1
                        if (bottomRightLife >= 0) bottomRightLife += 1
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Blast",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }

            // Info Button
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { showInfoDialog = true }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Bottom Row
        Row(
            modifier = Modifier
                .weight(0.475f)
                .fillMaxWidth()
        ) {
            // Bottom Left
            LifeCounter(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isRightSide = false,
                backgroundImageId = bottomLeftImage,
                baseId = bottomLeftBaseId,
                life = bottomLeftLife,
                playerName = playerNames[2],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[2] = newName }
                },
                onImageChange = { bottomLeftImage = it },
                onBaseChange = { bottomLeftBaseId = it },
                onLifeChange = { bottomLeftLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 2
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Black)
            )

            // Bottom Right
            LifeCounter(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isRightSide = true,
                backgroundImageId = bottomRightImage,
                baseId = bottomRightBaseId,
                life = bottomRightLife,
                playerName = playerNames[3],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[3] = newName }
                },
                onImageChange = { bottomRightImage = it },
                onBaseChange = { bottomRightBaseId = it },
                onLifeChange = { bottomRightLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 3
            )
        }

        if (showPlayerCountDialog) {
            PlayerCountDialog(
                onDismissRequest = { showPlayerCountDialog = false },
                onPlayerCountSelected = { count ->
                    showPlayerCountDialog = false
                }
            )
        }

        if (showInfoDialog) {
            InfoDialog(
                onDismissRequest = { showInfoDialog = false }
            )
        }
    }
}
