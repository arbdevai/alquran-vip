package com.arbdevai.quranvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arbdevai.quranvip.data.model.Day
import com.arbdevai.quranvip.ui.components.GlassCard
import com.arbdevai.quranvip.ui.theme.AmberAccent
import com.arbdevai.quranvip.ui.theme.TextPrimary
import com.arbdevai.quranvip.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NextPrayerCountdownCard(day: Day, modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    val nowStr = format.format(Date(currentTime))

    val times = day.times().filter { it.first in listOf("Imsak", "Subuh", "Zuhur", "Asar", "Magrib", "Isya") }
    var nextPrayer: Pair<String, String>? = null
    var currentPrayer: Pair<String, String>? = null

    for (i in times.indices) {
        if (nowStr < times[i].second) {
            nextPrayer = times[i]
            currentPrayer = if (i > 0) times[i - 1] else times.last() // simplified
            break
        }
    }
    if (nextPrayer == null && times.isNotEmpty()) {
        nextPrayer = times.first()
        currentPrayer = times.last()
    }

    if (nextPrayer != null) {
        // Calculate diff
        try {
            val calNow = Calendar.getInstance()
            val calNext = Calendar.getInstance()
            val parts = nextPrayer.second.split(":")
            calNext.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
            calNext.set(Calendar.MINUTE, parts[1].toInt())
            calNext.set(Calendar.SECOND, 0)
            
            if (calNext.before(calNow)) {
                calNext.add(Calendar.DAY_OF_MONTH, 1)
            }
            
            val diffMs = calNext.timeInMillis - calNow.timeInMillis
            val hours = diffMs / (1000 * 60 * 60)
            val mins = (diffMs / (1000 * 60)) % 60
            val secs = (diffMs / 1000) % 60
            val countdown = String.format("%02d:%02d:%02d", hours, mins, secs)

            GlassCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), containerColor = AmberAccent.copy(alpha = 0.1f), borderColor = AmberAccent.copy(alpha = 0.4f)) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Menuju ${nextPrayer.first}", style = MaterialTheme.typography.labelSmall, color = AmberAccent, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("- $countdown", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.ExtraBold)
                    }
                    Box(modifier = Modifier.size(48.dp).background(AmberAccent.copy(alpha = 0.2f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = AmberAccent)
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore parse errors
        }
    }
}
