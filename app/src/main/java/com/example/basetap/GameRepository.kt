// GameRepository.kt - SharedPreferences version
package dev.bearcat.basetap

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GameResult(
    val id: String,
    val winnerName: String,
    val winnerBaseId: Int,
    val winnerLeaderImage: String,
    val loserName: String,
    val loserBaseId: Int,
    val loserLeaderImage: String,
    val winnerFinalLife: Int,
    val loserFinalLife: Int,
    val gameDate: String,
    val gameDurationMinutes: Long?
)

class GameRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("game_results", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun saveGameResult(
        winnerName: String,
        winnerBaseId: Int,
        winnerLeaderImage: String,
        winnerLife: Int,
        loserName: String,
        loserBaseId: Int,
        loserLeaderImage: String,
        loserLife: Int,
        gameDurationMinutes: Long? = null
    ) = withContext(Dispatchers.IO) {
        val gameId = "game_${System.currentTimeMillis()}"
        val gameResult = JSONObject().apply {
            put("id", gameId)
            put("winnerName", winnerName)
            put("winnerBaseId", winnerBaseId)
            put("winnerLeaderImage", winnerLeaderImage)
            put("loserName", loserName)
            put("loserBaseId", loserBaseId)
            put("loserLeaderImage", loserLeaderImage)
            put("winnerFinalLife", winnerLife)
            put("loserFinalLife", loserLife)
            put("gameDate", dateFormat.format(Date()))
            put("gameDurationMinutes", gameDurationMinutes ?: JSONObject.NULL)
        }

        prefs.edit()
            .putString(gameId, gameResult.toString())
            .apply()
    }

    suspend fun getAllGameResults(): List<GameResult> = withContext(Dispatchers.IO) {
        val results = mutableListOf<GameResult>()
        val allEntries = prefs.all

        for ((key, value) in allEntries) {
            if (key.startsWith("game_") && value is String) {
                try {
                    val json = JSONObject(value)
                    results.add(
                        GameResult(
                            id = json.getString("id"),
                            winnerName = json.getString("winnerName"),
                            winnerBaseId = json.getInt("winnerBaseId"),
                            winnerLeaderImage = json.getString("winnerLeaderImage"),
                            loserName = json.getString("loserName"),
                            loserBaseId = json.getInt("loserBaseId"),
                            loserLeaderImage = json.getString("loserLeaderImage"),
                            winnerFinalLife = json.getInt("winnerFinalLife"),
                            loserFinalLife = json.getInt("loserFinalLife"),
                            gameDate = json.getString("gameDate"),
                            gameDurationMinutes = if (json.isNull("gameDurationMinutes")) null else json.getLong("gameDurationMinutes")
                        )
                    )
                } catch (e: Exception) {
                    // Skip malformed entries
                    continue
                }
            }
        }

        // Sort by date (newest first)
        results.sortedByDescending { it.gameDate }
    }

    suspend fun getPlayerStats(playerName: String): Pair<Int, Int> = withContext(Dispatchers.IO) {
        val allResults = getAllGameResults()
        val wins = allResults.count { it.winnerName == playerName }
        val losses = allResults.count { it.loserName == playerName }
        Pair(wins, losses)
    }

    suspend fun getPlayerGameHistory(playerName: String): List<GameResult> = withContext(Dispatchers.IO) {
        getAllGameResults().filter {
            it.winnerName == playerName || it.loserName == playerName
        }
    }

    suspend fun clearAllResults() = withContext(Dispatchers.IO) {
        val editor = prefs.edit()
        val allEntries = prefs.all

        for (key in allEntries.keys) {
            if (key.startsWith("game_")) {
                editor.remove(key)
            }
        }

        editor.apply()
    }

    suspend fun getGameCount(): Int = withContext(Dispatchers.IO) {
        prefs.all.count { it.key.startsWith("game_") }
    }
}