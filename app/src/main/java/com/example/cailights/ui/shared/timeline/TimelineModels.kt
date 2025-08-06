package com.example.cailights.ui.shared.timeline

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Generic timeline item that can represent any chronological event
 */
data class TimelineItem(
    val id: String,
    val date: String,
    val title: String,
    val description: String? = null,
    val isLeftAligned: Boolean = false,
    val isPinned: Boolean = false,
    val isClickable: Boolean = true
)

/**
 * Processed highlight with positioning information
 */
data class ProcessedHighlight(
    val item: com.example.cailights.ui.history.HighlightItem,
    val originalIndex: Int,
    val isLeftAligned: Boolean
)

/**
 * Timeline configuration for customizing appearance and behavior
 */
@Composable
fun getTimelineConfig(): TimelineConfig {
    return TimelineConfig(
        showConnectingLines = true,
        cardBackgroundAlpha = 0.15f,
        nodeColor = MaterialTheme.colorScheme.primary,
        cardBackgroundColor = MaterialTheme.colorScheme.secondary,
        textColor = MaterialTheme.colorScheme.onBackground,
        dateTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        pinnedCardBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        pinnedCardBackgroundAlpha = 0.3f
    )
}

data class TimelineConfig(
    val showConnectingLines: Boolean = true,
    val cardBackgroundAlpha: Float = 0.15f,
    val nodeColor: androidx.compose.ui.graphics.Color,
    val cardBackgroundColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color,
    val dateTextColor: androidx.compose.ui.graphics.Color,
    val pinnedCardBackgroundColor: androidx.compose.ui.graphics.Color,
    val pinnedCardBackgroundAlpha: Float = 0.3f
)

/**
 * Extension functions to convert between different data models
 */
fun com.example.cailights.ui.history.HighlightItem.toTimelineItem(isLeftAligned: Boolean = false): TimelineItem {
    return TimelineItem(
        id = this.id.toString(),
        date = this.date,
        title = this.title,
        description = this.fullContent,
        isLeftAligned = isLeftAligned,
        isPinned = false,
        isClickable = true
    )
}

fun com.example.cailights.ui.profile.TimelineEvent.toTimelineItem(): TimelineItem {
    return TimelineItem(
        id = this.id.toString(),
        date = this.date,
        title = this.title,
        description = null,
        isLeftAligned = this.isLeft,
        isPinned = false,
        isClickable = false
    )
}

fun com.example.cailights.ui.profile.PinnedAchievement.toTimelineItem(): TimelineItem {
    return TimelineItem(
        id = "pinned",
        date = this.date,
        title = this.title,
        description = this.description,
        isLeftAligned = false,
        isPinned = true,
        isClickable = false
    )
}
