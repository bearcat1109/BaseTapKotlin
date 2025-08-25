package dev.bearcat.basetap

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf<GameScreen>(GameScreen.Welcome) }
            var initiativePlayer by remember { mutableStateOf<Int?>(null) }

            // First-time welcome popup state
            val context = LocalContext.current
            val sharedPreferences = remember {
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            }
            val isFirstRun = remember {
                sharedPreferences.getBoolean("is_first_run", true)
            }

            var showFirstTimePopup by remember { mutableStateOf(isFirstRun) }

            // Mark first run as completed if showing popup
            LaunchedEffect(showFirstTimePopup) {
                if (showFirstTimePopup) {
                    sharedPreferences.edit().putBoolean("is_first_run", false).apply()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        // Compare player count when in PlayerLayout state
                        val isIncrease = when {
                            initialState is GameScreen.Welcome && targetState is GameScreen.PlayerLayout -> true
                            initialState is GameScreen.PlayerLayout && targetState is GameScreen.PlayerLayout ->
                                (initialState as GameScreen.PlayerLayout).playerCount <
                                        (targetState as GameScreen.PlayerLayout).playerCount
                            initialState is GameScreen.PlayerLayout && targetState is GameScreen.Welcome -> false
                            else -> false
                        }

                        if (isIncrease) {
                            slideInHorizontally { width -> width } +
                                    fadeIn() togetherWith
                                    slideOutHorizontally { width -> -width } +
                                    fadeOut()
                        } else {
                            slideInHorizontally { width -> -width } +
                                    fadeIn() togetherWith
                                    slideOutHorizontally { width -> width } +
                                    fadeOut()
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }
                ) { screen ->
                    when (screen) {
                        is GameScreen.Welcome -> WelcomeScreen(
                            onPlayerCountSelected = { playerCount ->
                                currentScreen = GameScreen.PlayerLayout(playerCount)
                            }
                        )
                        is GameScreen.PlayerLayout -> when (screen.playerCount) {
                            1 -> OnePlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            2 -> TwoPlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            3 -> ThreePlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            4 -> FourPlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            5 -> FivePlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            6 -> SixPlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                            else -> TwoPlayerLayout(
                                onShowPlayerCount = {
                                    currentScreen = GameScreen.Welcome
                                },
                                initiativePlayer = initiativePlayer,
                                onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                            )
                        }
                        // Add an exhaustive else branch defaulting to two-player layout
                        else -> TwoPlayerLayout(
                            onShowPlayerCount = {
                                currentScreen = GameScreen.Welcome
                            },
                            initiativePlayer = initiativePlayer,
                            onInitiativeClaimed = { playerId -> initiativePlayer = playerId }
                        )
                    }
                    }
                }

            if (showFirstTimePopup) {
                FirstTimeWelcomeDialog(
                    onDismiss = { showFirstTimePopup = false }
                )
            }
            }
        }
    }

// Sealed class to manage screen states
sealed class GameScreen {
    object Welcome : GameScreen()
    data class PlayerLayout(val playerCount: Int) : GameScreen()
}


// Welcome screen
@Composable
fun WelcomeScreen(
    onPlayerCountSelected: (Int) -> Unit
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    // Welcome messages
    val welcomeTranslations = listOf(
        "Welcome to BaseTap!",
        "ようこそ",
        "ᎣᏏᏲ!",
        "¡Bienvenido!"
    )
    var currentWelcomeText by remember { mutableStateOf(welcomeTranslations.first()) }
    // Cycle through translations
    LaunchedEffect(Unit) {
        while(true) {
            delay(3000) // Change text every 3 seconds
            val currentIndex = welcomeTranslations.indexOf(currentWelcomeText)
            val nextIndex = (currentIndex + 1) % welcomeTranslations.size
            currentWelcomeText = welcomeTranslations[nextIndex]
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.drawable.hyperspacegif,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // App Title
            Text(
                text = currentWelcomeText,
                color = Color.White,
                fontSize = 32.sp,
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(color = Color.Gray, blurRadius = 12f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Subtitle
            Text(
                text = "We hope you enjoy your stay.\n" + "Legends of the Force Leaders now available. \n" + "May the Force be with you!",
                color = Color.White,
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Player Count Selection
            Text(
                text = "Select Number of Players",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Player Count Buttons - Circular layout in pairs
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row: 1 and 2 players
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onPlayerCountSelected(1) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "1",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onPlayerCountSelected(2) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "2",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Second row: 3 and 4 players
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onPlayerCountSelected(3) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "3",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onPlayerCountSelected(4) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "4",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Third row: 5 and 6 players
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onPlayerCountSelected(5) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "5",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onPlayerCountSelected(6) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "6",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Info or About button
            TextButton(
                onClick = { showInfoDialog = true },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "About BaseTap",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Info Dialog
        if (showInfoDialog) {
            InfoDialog(
                onDismissRequest = { showInfoDialog = false }
            )
        }
    }
}

@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("About BaseTap") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                        "All images and Star Wars property of Disney and Fantasy Flight Games. \n\n"+
                        "BaseTap is a life counter app designed for Star Wars: Unlimited." +
                            "This app was created as a personal project, and is and will always be free.I do not own any of the images or characters used.\n\n" +
                            "Version 1.6 - LOF\n" +
                            "Created by Bearcat!\n" +
                            "Youtube.com/@bearcatmakesgames\n\n" +
                            "Feedback is awesome. Please email feedback or bugs to bearcatfeedback@gmail.com\n\n"+
                            "Want to support the development of BaseTap? You rock! PayPal.me/BearcatCodes"

                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}

// First Time Welcome Dialog
@Composable
fun FirstTimeWelcomeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Welcome to BaseTap!") },
        text = {
            Text(
                "Thank you for downloading BaseTap, a life counter app for Star Wars: Unlimited!\n\n" +
                        "Click on each Player Name to change their leader and base. Tap on the plus side of each counter to add damage, and minus to remove. Click on the People icon to return to player count select screen. The Blast token button deals 1 damage to each base. The Info button will tell you more about the app. Press Reset to return Base health counts to 0. \n\n" +
                        "Tap anywhere to continue to the app. We hope you enjoy your stay!"
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Get Started")
            }
        }
    )
}