package com.example.cailights.ui.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.history.HighlightItem

@Composable
fun TimelineSection(
    highlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TimelineContent(
            highlights = highlights,
            onHighlightClick = onHighlightClick
        )
    }
}


@Composable
private fun TimelineContent(
    highlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit
) {
    Column {
        highlights.forEachIndexed { index, processedHighlight ->
            TimelineRow(
                highlight = processedHighlight.item,
                isLeftAligned = processedHighlight.isLeftAligned,
                isFirst = index == 0,
                isLast = index == highlights.lastIndex,
                onHighlightClick = onHighlightClick
            )
        }
    }
}

@Composable
private fun TimelineRow(
    highlight: HighlightItem,
    isLeftAligned: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onHighlightClick: (HighlightItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeftAligned) {
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
            TimelineIndicator(
                isFirst = isFirst,
                isLast = isLast,
                modifier = Modifier.padding(top = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.weight(1f))
            TimelineIndicator(
                isFirst = isFirst,
                isLast = isLast,
                modifier = Modifier.padding(top = 20.dp)
            )
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun TimelineIndicator(
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isFirst) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .background(Color(0xFFE0E0E0))
            )
        }
        
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = Color(0xFF4A4AFF),
                    shape = RoundedCornerShape(6.dp)
                )
        )
        
        if (!isLast) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(40.dp)
                    .background(Color(0xFFE0E0E0))
            )
        }
    }
}

@Composable
private fun TimelineCard(
    highlight: HighlightItem,
    onHighlightClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = highlight.date,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2A2A2A),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onHighlightClick(highlight) }
        ) {
            Text(
                text = highlight.title,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Data class for processed highlights
data class ProcessedHighlight(
    val item: HighlightItem,
    val originalIndex: Int,
    val isLeftAligned: Boolean
)
