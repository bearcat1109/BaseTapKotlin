package dev.bearcat.basetap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun OnePlayerLayout(
    onShowPlayerCount: () -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit
) {
    var playerLife by remember { mutableStateOf(0) }
    var playerImage by remember { mutableStateOf(R.drawable.darth_maul_sith_revealed_showcase) }
    var playerBaseId by remember { mutableStateOf(47) }
    var playerName by remember { mutableStateOf("Player 1") }

    // Info button
    var showInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Controls - positioned at the top in the middle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        playerLife = 0
                    }
            )

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Select Player Count",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onShowPlayerCount()
                    }
            )

            Text(
                text = "Blast",
                color = Color.Red,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable {
                        if (playerLife >= 0) playerLife += 1
                    }
            )

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        showInfoDialog = true
                    }
            )
        }

        // Single Player Counter - takes up the rest of the screen
        TwoPlayerCounter(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            isTopPlayer = false, // Use normal orientation (not upside down)
            backgroundImageId = playerImage,
            baseId = playerBaseId,
            life = playerLife,
            playerName = playerName,
            onNameChange = { newName ->
                playerName = newName
            },
            onImageChange = { playerImage = it },
            onBaseChange = { playerBaseId = it },
            onLifeChange = { playerLife = it },
            initiativePlayer = initiativePlayer,
            onInitiativeClaimed = onInitiativeClaimed,
            playerId = 0
        )
    }

    // Info Dialog
    if (showInfoDialog) {
        InfoDialog(
            onDismissRequest = { showInfoDialog = false }
        )
    }
}