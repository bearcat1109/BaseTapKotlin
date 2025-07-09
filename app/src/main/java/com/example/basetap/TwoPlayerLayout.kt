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
fun TwoPlayerLayout(
    onShowPlayerCount: () -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit
) {
    var topLife by remember { mutableStateOf(0) }
    var bottomLife by remember { mutableStateOf(0) }

    var topImage by remember { mutableStateOf(R.drawable.darth_maul_sith_revealed_showcase) }
    var bottomImage by remember { mutableStateOf(R.drawable.qui_gon_jinn_student_of_the_living_force_showcase) }

    var topBaseId by remember { mutableStateOf(47) }
    var bottomBaseId by remember { mutableStateOf(28) }

    var playerNames by remember { mutableStateOf(List(2) { "Player ${it + 1}" }) }

    // Info button
    var showInfoDialog by remember { mutableStateOf(false) }

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
            },
            onImageChange = { topImage = it },
            onBaseChange = { topBaseId = it },
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
                        topLife = 0
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
                        if (topLife >= 0) topLife += 1
                        if (bottomLife >= 0) bottomLife += 1
                    }
            )

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Select Player Count",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable{
                        showInfoDialog = true
                    }
            )

            if(showInfoDialog){
                InfoDialog(
                    onDismissRequest = { showInfoDialog = false }
                )
            }
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
            },
            onImageChange = { bottomImage = it },
            onBaseChange = { bottomBaseId = it },
            onLifeChange = { bottomLife = it },
            initiativePlayer = initiativePlayer,
            onInitiativeClaimed = onInitiativeClaimed,
            playerId = 1
        )
    }
}
