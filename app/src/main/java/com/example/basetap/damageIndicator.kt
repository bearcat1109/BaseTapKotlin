
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun DamageIndicator(
    damage: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Text(
            text = if (damage > 0) "+$damage" else "$damage",
            color = if (damage > 0) Color.Green else Color.Red,
            fontSize = 24.sp
        )
    }
}

// Class to handle damage accumulation and timing
class DamageAccumulator {
    private var lastDamageTime = 0L
    private var accumulatedDamage = 0

    fun addDamage(damage: Int): Int {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDamageTime > 1000) {
            // Reset if more than 1 second has passed
            accumulatedDamage = damage
        } else {
            accumulatedDamage += damage
        }
        lastDamageTime = currentTime
        return accumulatedDamage
    }
}
