package com.example.cailights.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.history.components.TimelineRow
import com.example.cailights.ui.history.components.TimelineSection
import com.example.cailights.ui.shared.timeline.ProcessedHighlight
import com.example.cailights.ui.history.utils.HighlightProcessor

@Composable
fun ProfileTimelineComponent(
    historyHighlights: List<HighlightItem>,
    onHighlightClick: ((HighlightItem) -> Unit)? = null,
    onHighlightLongClick: ((HighlightItem) -> Unit)? = null
) {
    // Process highlights for timeline display (same logic as history page)
    val processedHighlights = HighlightProcessor.processHighlightsForTimeline(historyHighlights)
    
    // Use timeline section with custom padding for profile
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        processedHighlights.forEachIndexed { index, processedHighlight ->
            TimelineRow(
                highlight = processedHighlight.item,
                isLeftAligned = processedHighlight.isLeftAligned,
                isFirst = index == 0,
                isLast = index == processedHighlights.lastIndex,
                onHighlightClick = onHighlightClick ?: { },
                onHighlightLongClick = onHighlightLongClick ?: { }
            )
        }
    }
}
