package com.example.basetap

import android.content.Context
import android.content.Intent
import dev.bearcat.basetap.Bases

fun sendGameResultEmail(
    context: Context,
    player1Name: String,
    player2Name: String,
    player1Life: Int,
    player2Life: Int,
    player1BaseId: Int,
    player2BaseId: Int,
    player1LeaderImage: Int,
    player2LeaderImage: Int,
    winnerName: String,
    gameDurationMinutes: Long
) {
    val player1Leader = getLeaderNameFromImage(context, player1LeaderImage)
    val player2Leader = getLeaderNameFromImage(context, player2LeaderImage)
    val player1Base = Bases.findById(player1BaseId)?.name ?: "Unknown Base"
    val player2Base = Bases.findById(player2BaseId)?.name ?: "Unknown Base"

    val emailSubject = "Star Wars Unlimited Game Result - $winnerName Wins!"
    val emailBody = """
        Game Results:
        ═══════════════════════════════════
        
        Winner: $winnerName
        Game Duration: $gameDurationMinutes minutes
        
        Player 1: $player1Name
        - Leader: $player1Leader
        - Base: $player1Base (ID: $player1BaseId)
        - Final Life: $player1Life
        
        Player 2: $player2Name
        - Leader: $player2Leader
        - Base: $player2Base (ID: $player2BaseId)
        - Final Life: $player2Life
        
        ═══════════════════════════════════
        Sent from BaseTap - Star Wars Unlimited Life Counter
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822" // Ensures only email apps respond
        putExtra(Intent.EXTRA_EMAIL, arrayOf("gabrielberres9@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        putExtra(Intent.EXTRA_TEXT, emailBody)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Send game results via..."))
    } catch (e: Exception) {
        // Handle the case where no email app is available
        // You might want to show a Toast or Snackbar here
    }
}

// Helper function - add this if it doesn't exist
fun getLeaderNameFromImage(context: Context, imageResource: Int): String {
    return try {
        val resourceName = context.resources.getResourceEntryName(imageResource)
        // Convert resource name to readable format
        resourceName.replace('_', ' ')
            .split(' ')
            .joinToString(" ") { it.capitalize() }
    } catch (e: Exception) {
        "Unknown Leader"
    }
}