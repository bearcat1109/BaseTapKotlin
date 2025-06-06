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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
fun SixPlayerLayout(
    onShowPlayerCount: () -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit
) {
    // Life totals
    var topLeftLife by remember { mutableStateOf(0) }
    var topRightLife by remember { mutableStateOf(0) }
    var midLeftLife by remember { mutableStateOf(0) }
    var midRightLife by remember { mutableStateOf(0) }
    var bottomLeftLife by remember { mutableStateOf(0) }
    var bottomRightLife by remember { mutableStateOf(0) }

    // Image resources
    var topLeftImage by remember { mutableStateOf(R.drawable.boba_fett_any_methods_necessary_showcase) }
    var topRightImage by remember { mutableStateOf(R.drawable.han_solo_never_tell_me_the_odds_showcase) }
    var midLeftImage by remember { mutableStateOf(R.drawable.poe_dameron_i_can_fly_anything_showcase) }
    var midRightImage by remember { mutableStateOf(R.drawable.darth_vader_victor_squadron_leader) }
    var bottomLeftImage by remember { mutableStateOf(R.drawable.lando_calrissian_buying_time_showcase) }
    var bottomRightImage by remember { mutableStateOf(R.drawable.captain_phasma_chrome_dome_showcase) }

    // Base IDs
    var topLeftBaseId by remember { mutableStateOf(1) }
    var topRightBaseId by remember { mutableStateOf(1) }
    var midLeftBaseId by remember { mutableStateOf(1) }
    var midRightBaseId by remember { mutableStateOf(1) }
    var bottomLeftBaseId by remember { mutableStateOf(1) }
    var bottomRightBaseId by remember { mutableStateOf(1) }

    // Player names
    var playerNames by remember { mutableStateOf(List(6) { "Player ${it + 1}" }) }

    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Four Players Section (0.65f)
        Column(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxWidth()
        ) {
            // Top Row
            Row(
                modifier = Modifier
                    .weight(0.5f)
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

            // Middle Row
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {
                // Middle Left
                LifeCounter(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    isRightSide = false,
                    backgroundImageId = midLeftImage,
                    baseId = midLeftBaseId,
                    life = midLeftLife,
                    playerName = playerNames[2],
                    onNameChange = { newName ->
                        playerNames = playerNames.toMutableList().also { it[2] = newName }
                    },
                    onImageChange = { midLeftImage = it },
                    onBaseChange = { midLeftBaseId = it },
                    onLifeChange = { midLeftLife = it },
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

                // Middle Right
                LifeCounter(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    isRightSide = true,
                    backgroundImageId = midRightImage,
                    baseId = midRightBaseId,
                    life = midRightLife,
                    playerName = playerNames[3],
                    onNameChange = { newName ->
                        playerNames = playerNames.toMutableList().also { it[3] = newName }
                    },
                    onImageChange = { midRightImage = it },
                    onBaseChange = { midRightBaseId = it },
                    onLifeChange = { midRightLife = it },
                    initiativePlayer = initiativePlayer,
                    onInitiativeClaimed = onInitiativeClaimed,
                    playerId = 3
                )
            }
        }

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
                        topLeftLife = 0
                        topRightLife = 0
                        midLeftLife = 0
                        midRightLife = 0
                        bottomLeftLife = 0
                        bottomRightLife = 0
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
                        if (topLeftLife >= 0) topLeftLife += 1
                        if (topRightLife >= 0) topRightLife += 1
                        if (midLeftLife >= 0) midLeftLife += 1
                        if (midRightLife >= 0) midRightLife += 1
                        if (bottomLeftLife >= 0) bottomLeftLife += 1
                        if (bottomRightLife >= 0) bottomRightLife += 1
                    }
            )

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "App Info",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        showInfoDialog = true
                    }
            )
        }

        // Bottom Two Players Section (0.3f)
        Row(
            modifier = Modifier
                .weight(0.3f)
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
                playerName = playerNames[4],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[4] = newName }
                },
                onImageChange = { bottomLeftImage = it },
                onBaseChange = { bottomLeftBaseId = it },
                onLifeChange = { bottomLeftLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 4
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
                playerName = playerNames[5],
                onNameChange = { newName ->
                    playerNames = playerNames.toMutableList().also { it[5] = newName }
                },
                onImageChange = { bottomRightImage = it },
                onBaseChange = { bottomRightBaseId = it },
                onLifeChange = { bottomRightLife = it },
                initiativePlayer = initiativePlayer,
                onInitiativeClaimed = onInitiativeClaimed,
                playerId = 5
            )
        }

        if (showInfoDialog) {
            InfoDialog(
                onDismissRequest = { showInfoDialog = false }
            )
        }
    }
}
