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
fun ThreePlayerLayout(
    onShowPlayerCount: () -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit
) {
    // Life totals
    var topLeftLife by remember { mutableStateOf(0) }
    var topRightLife by remember { mutableStateOf(0) }
    var bottomLife by remember { mutableStateOf(0) }

    // Image resources
    var topLeftImage by remember { mutableStateOf(R.drawable.boba_fett_any_methods_necessary_showcase) }
    var topRightImage by remember { mutableStateOf(R.drawable.han_solo_never_tell_me_the_odds_showcase) }
    var bottomImage by remember { mutableStateOf(R.drawable.darth_vader_victor_squadron_leader) }

    // Bases
    var topLeftBaseId by remember { mutableStateOf(1) }
    var topRightBaseId by remember { mutableStateOf(1) }
    var bottomBaseId by remember { mutableStateOf(1) }

    // Player names
    var playerNames by remember { mutableStateOf(List(3) { "Player ${it + 1}" }) }

    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Row (split in two like 4-player layout)
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
                        bottomLife = 0
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
                        if (bottomLife >= 0) bottomLife += 1
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

        // Bottom single player (like 2-player layout)
        TwoPlayerCounter(
            modifier = Modifier
                .weight(0.475f)
                .fillMaxWidth(),
            isTopPlayer = false,
            backgroundImageId = bottomImage,
            baseId = bottomBaseId,
            life = bottomLife,
            playerName = playerNames[2],
            onNameChange = { newName ->
                playerNames = playerNames.toMutableList().also { it[2] = newName }
            },
            onImageChange = { bottomImage = it },
            onBaseChange = { bottomBaseId = it },
            onLifeChange = { bottomLife = it },
            initiativePlayer = initiativePlayer,
            onInitiativeClaimed = onInitiativeClaimed,
            playerId = 2
        )

        if (showInfoDialog) {
            InfoDialog(
                onDismissRequest = { showInfoDialog = false }
            )
        }
    }
}
