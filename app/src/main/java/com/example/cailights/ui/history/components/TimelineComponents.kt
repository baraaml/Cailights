package com.example.cailights.ui.history.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.shared.timeline.ProcessedHighlight

@Composable
fun TimelineSection(
    highlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        highlights.forEachIndexed { index, processedHighlight ->
            TimelineRow(
                highlight = processedHighlight.item,
                isLeftAligned = processedHighlight.isLeftAligned,
                isFirst = index == 0,
                isLast = index == highlights.lastIndex,
                onHighlightClick = onHighlightClick,
                onHighlightLongClick = onHighlightLongClick
            )
        }
    }
}

@Composable
fun TimelineRow(
    highlight: HighlightItem,
    isLeftAligned: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeftAligned) {
            // Left: Card
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onHighlightLongClick = onHighlightLongClick
            )
            // Center: Node with connecting lines
            TimelineNode(
                isFirst = isFirst,
                isLast = isLast,
                modifier = Modifier.fillMaxHeight()
            )
            // Right: Date
            DateLabel(
                date = highlight.date,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        } else {
            // Left: Date
            DateLabel(
                date = highlight.date,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            // Center: Node with connecting lines
            TimelineNode(
                isFirst = isFirst,
                isLast = isLast,
                modifier = Modifier.fillMaxHeight()
            )
            // Right: Card
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onHighlightLongClick = onHighlightLongClick
            )
        }
    }
}

@Composable
private fun DateLabel(
    date: String,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = textAlign,
        modifier = modifier.padding(top = 4.dp) // Align text with the dot
    )
}

// Alternative approach: Use a single continuous line with overlay dots
@Composable
private fun TimelineNode(
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(24.dp)
            .fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Continuous vertical line (except for first and last partial sections)
        if (!isFirst || !isLast) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )
        }

        // Top section - hide line for first item
        if (isFirst) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp) // Half height to center the dot
                    .background(Color.Transparent)
                    .align(Alignment.TopCenter)
            )
        }

        // Bottom section - hide line for last item
        if (isLast) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp) // Half height from center down
                    .background(Color.Transparent)
                    .align(Alignment.BottomCenter)
            )
        }

        // Central dot - positioned at the center
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimelineCard(
    highlight: HighlightItem,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit, // New parameter for long click
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .combinedClickable(
                onClick = { onHighlightClick(highlight) },
                onLongClick = { onHighlightLongClick(highlight) } // Handle long click
            ),
        shadowElevation = 2.dp
    ){
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = highlight.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold
            )

            val shortDescription = remember(highlight.fullContent) {
                highlight.fullContent.take(60) + if (highlight.fullContent.length > 60) "..." else ""
            }

            if (shortDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = shortDescription,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
