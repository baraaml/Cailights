import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.Dp
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

// Data class for processed highlights
data class ProcessedHighlight(
    val item: HighlightItem,
    val originalIndex: Int,
    val isLeftAligned: Boolean
)

@Composable
fun TimelineSection(
    highlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
                showTopLine = index > 0,
                showBottomLine = index < highlights.lastIndex,
                onHighlightClick = onHighlightClick
            )
        }
    }
}

@Composable
private fun TimelineRow(
    highlight: HighlightItem,
    isLeftAligned: Boolean,
    showTopLine: Boolean,
    showBottomLine: Boolean,
    onHighlightClick: (HighlightItem) -> Unit
) {
    val timelineHeight = 100.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(timelineHeight),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeftAligned) {
            // Left-aligned layout: Card - Timeline+Date - Empty space
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, top = 16.dp)
            )
            TimelineWithDate(
                date = highlight.date,
                showTopLine = showTopLine,
                showBottomLine = showBottomLine,
                totalHeight = timelineHeight,
                isLeftAligned = true
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            // Right-aligned layout: Empty space - Timeline+Date - Card
            Spacer(modifier = Modifier.weight(1f))
            TimelineWithDate(
                date = highlight.date,
                showTopLine = showTopLine,
                showBottomLine = showBottomLine,
                totalHeight = timelineHeight,
                isLeftAligned = false
            )
            TimelineCard(
                highlight = highlight,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 16.dp)
            )
        }
    }
}

@Composable
private fun TimelineWithDate(
    date: String,
    showTopLine: Boolean,
    showBottomLine: Boolean,
    totalHeight: Dp,
    isLeftAligned: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(totalHeight),
        verticalAlignment = Alignment.Top
    ) {
        if (isLeftAligned) {
            // For left-aligned cards: Timeline - Date
            TimelineIndicator(
                showTopLine = showTopLine,
                showBottomLine = showBottomLine,
                totalHeight = totalHeight,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = date,
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp, top = 20.dp)
            )
        } else {
            // For right-aligned cards: Date - Timeline
            Text(
                text = date,
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(end = 8.dp, top = 20.dp)
            )
            TimelineIndicator(
                showTopLine = showTopLine,
                showBottomLine = showBottomLine,
                totalHeight = totalHeight,
                modifier = Modifier.padding(top = 16.dp)
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
    // Highlight card with bubble shape (no date label here anymore)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1F1F1F),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onHighlightClick(highlight) },
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Main title
            Text(
                text = highlight.title,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Secondary description (truncated version of fullContent)
            val shortDescription = highlight.fullContent.take(60) +
                    if (highlight.fullContent.length > 60) "..." else ""

            if (shortDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = shortDescription,
                    color = Color(0xFFB0B0B0),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun TimelineIndicator(
    showTopLine: Boolean,
    showBottomLine: Boolean,
    totalHeight: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(totalHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top connecting line
        if (showTopLine) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(16.dp)
                    .background(Color(0xFFE0E0E0))
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Central dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = Color(0xFF4A4AFF),
                    shape = RoundedCornerShape(5.dp)
                )
        )

        // Bottom connecting line - fill remaining space
        if (showBottomLine) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(Color(0xFFE0E0E0))
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}