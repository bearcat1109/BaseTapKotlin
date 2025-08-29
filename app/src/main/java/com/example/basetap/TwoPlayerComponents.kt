package dev.bearcat.basetap

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TwoPlayerCounter(
    modifier: Modifier = Modifier,
    isRightSide: Boolean = false,
    isTopPlayer: Boolean = false,
    backgroundImageId: Int,
    baseId: Int,
    life: Int,
    playerName: String,
    onNameChange: (String) -> Unit,
    onImageChange: (Int) -> Unit,
    onLifeChange: (Int) -> Unit,
    initiativePlayer: Int?,
    onInitiativeClaimed: (Int) -> Unit,
    playerId: Int,
    onBaseChange: (Int) -> Unit,
) {
    var showNameDialog by remember { mutableStateOf(false) }
    var showDamageIndicator by remember { mutableStateOf(false) }
    var currentDamage by remember { mutableStateOf(0) }
    val damageAccumulator = remember { DamageAccumulator() }
    val coroutineScope = rememberCoroutineScope()
    // Base Max Health (For K.O. detection)
    val baseMaxHealth = Bases.findById(baseId)?.maxHealth ?: 30
    var isDead by remember { mutableStateOf(false) }

    // For shaking
    var shakeKey by remember { mutableStateOf(0f) }
    //var shouldShake by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = shakeKey,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // For player name color based on selected base
    val baseName = Bases.findById(baseId)?.baseName ?: "Select Base"
    val baseTypeColor = getBaseTypeColor(baseName)

    fun handleLifeChange(newDamage: Int) {
        // If player is already dead (at or above max health), do nothing
        // +1 to allow for accidents
        if (life >= baseMaxHealth + 1) return

        if (life + newDamage >= 0) {
            val accumulatedDamage = damageAccumulator.addDamage(newDamage)
            currentDamage = accumulatedDamage
            showDamageIndicator = true
            onLifeChange(life + newDamage)

            // Trigger shake animation when taking damage
            if (newDamage > 0) {
                // Toggle between 1f and 2f to force animation restart
                shakeKey = if (shakeKey == 1f) 2f else 1f
            }

            coroutineScope.launch {
                delay(3000)
                showDamageIndicator = false
            }
        }
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (if (shakeKey != 0f) ((shakeKey - shakeOffset) * 15).roundToInt() else 0),
                    y = 0
                )
            }
    ) {
        // Background Image
        Image(
            painter = painterResource(id = backgroundImageId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .run {
                    if (isTopPlayer) rotate(180f) else this
                },
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.Center
        )

        // Dead player overlay
        if (life >= baseMaxHealth) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
            )
        }

        // Card Banner - positioned at the BOTTOM of the section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .run {
                    if (isTopPlayer) rotate(180f) else this
                }
        ) {
            CardBanner(
                modifier = Modifier
                    .align(if (isTopPlayer) Alignment.TopCenter else Alignment.BottomCenter),
                baseId = baseId
            )
        }

        // Clickable areas for life adjustment using Row
        Row(modifier = Modifier.fillMaxSize()) {
            if (isTopPlayer) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { handleLifeChange(1) }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { handleLifeChange(-1) }
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { handleLifeChange(-1) }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { handleLifeChange(1) }
                )
            }
        }

        // Life total with damage indicator
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.rotate(if (isTopPlayer) 180f else 0f)
            ) {
                Text(
                    text = if (life >= baseMaxHealth) getRandomDeathMessage() else "-$life+",
                    color = if (life >= baseMaxHealth) Color.Red else Color.White,
                    fontSize = if (life >= baseMaxHealth) 30.sp else 80.sp,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(color = Color.Black, blurRadius = 12f),
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 3,
                    softWrap = true,
                )

                DamageIndicator(
                    damage = currentDamage,
                    isVisible = showDamageIndicator,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Player name and initiative section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = if (isTopPlayer) Alignment.BottomCenter else Alignment.TopCenter
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(if (isTopPlayer) 180f else 0f)
            ) {
                // Initiative Token
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = if (initiativePlayer == playerId)
                                Color.Yellow.copy(alpha = 0.7f)
                            else
                                Color.Black.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { onInitiativeClaimed(playerId) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.initiative_token),
                        contentDescription = "Initiative Token",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Player Name
                Text(
                    text = playerName,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = baseTypeColor.copy(alpha = 0.7f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { showNameDialog = true }
                        .padding(16.dp),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(color = Color.Black, blurRadius = 12f)
                    ),
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
    }


if (showNameDialog) {
    LeaderNameDialog(
        initialName = playerName,
        currentImageId = backgroundImageId,
        baseId = baseId,
        onDismissRequest = { showNameDialog = false },
        onConfirm = {
            onNameChange(it)
            showNameDialog = false
        },
        onImageChange = onImageChange,
        onBaseChange = onBaseChange
    )
}
}