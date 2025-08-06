package com.example.cailights.ui.shared.timeline

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimelineCard(
    item: TimelineItem,
    config: TimelineConfig,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val cardModifier = if (item.isClickable && onClick != null) {
        modifier.clickable { onClick() }
    } else {
        modifier
    }

    if (item.isPinned) {
        PinnedTimelineCard(
            item = item,
            config = config,
            modifier = cardModifier
        )
    } else {
        RegularTimelineCard(
            item = item,
            config = config,
            modifier = cardModifier
        )
    }
}

@Composable
private fun PinnedTimelineCard(
    item: TimelineItem,
    config: TimelineConfig,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = config.pinnedCardBackgroundColor.copy(alpha = config.pinnedCardBackgroundAlpha)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    color = config.textColor
                )
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    modifier = Modifier.size(16.dp),
                    tint = config.nodeColor
                )
            }
            
            item.description?.let { description ->
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = config.dateTextColor,
                    lineHeight = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = item.date,
                    fontSize = 12.sp,
                    color = config.dateTextColor
                )
            }
        }
    }
}

@Composable
private fun RegularTimelineCard(
    item: TimelineItem,
    config: TimelineConfig,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = config.cardBackgroundColor.copy(alpha = config.cardBackgroundAlpha)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                color = config.textColor,
                lineHeight = 16.sp,
                fontWeight = if (item.description != null) FontWeight.Medium else FontWeight.Normal
            )
            
            item.description?.let { description ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = config.dateTextColor,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
