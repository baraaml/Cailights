// ActivityGraphComponent.kt
package com.example.cailights.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.history.HighlightItem
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Composable
fun ActivityGraphComponent(
    highlights: List<HighlightItem>,
    modifier: Modifier = Modifier
) {
    val activityData = remember(highlights) {
        generateActivityData(highlights)
    }
    val currentYear = remember {
        LocalDate.now().year
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Activity in $currentYear",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // GitHub-style activity grid: 7 days per week
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 days per week
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            items(activityData) { dayActivity ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(2.dp))
                        .background(getActivityColor(dayActivity.intensity))
                )
            }
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Less",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                repeat(5) { level ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(getActivityColor(level / 4f))
                    )
                }
            }
            Text(
                text = "More",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun getActivityColor(intensity: Float): androidx.compose.ui.graphics.Color {
    return when {
        intensity == 0f -> MaterialTheme.colorScheme.surfaceVariant
        intensity <= 0.25f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        intensity <= 0.5f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        intensity <= 0.75f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        else -> MaterialTheme.colorScheme.primary
    }
}

data class DayActivity(
    val date: LocalDate,
    val intensity: Float
)

private fun generateActivityData(highlights: List<HighlightItem>): List<DayActivity> {
    val today = LocalDate.now()
    val yearAgo = today.minusYears(1).with(DayOfWeek.MONDAY) // Align to Monday
    val totalDays = ChronoUnit.DAYS.between(yearAgo, today).toInt() + 1

    // Count highlights per day
    val highlightCounts = highlights
        .mapNotNull { highlight ->
            runCatching {
                Instant.ofEpochMilli(highlight.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }.getOrNull()
        }
        .groupBy { it }
        .mapValues { it.value.size }

    // Find max count for normalization
    val maxCount = (highlightCounts.values.maxOrNull() ?: 1).coerceAtLeast(1)

    // Generate activity data for each day
    return (0 until totalDays).map { dayOffset ->
        val date = yearAgo.plusDays(dayOffset.toLong())
        val count = highlightCounts[date] ?: 0
        val intensity = if (count == 0) 0f else (count.toFloat() / maxCount.toFloat())
        DayActivity(date, intensity)
    }
}