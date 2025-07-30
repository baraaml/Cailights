package com.example.cailights.ui.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.history.HighlightItem

@Composable
fun HighlightDetailCard(
    highlight: HighlightItem,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HighlightHeader(
            title = highlight.title,
            date = highlight.date
        )
        Spacer(modifier = Modifier.height(16.dp))
        HighlightContent(content = highlight.fullContent)
        Spacer(modifier = Modifier.height(24.dp))
        HighlightActions(onShareClick = onShareClick)
    }
}

@Composable
private fun HighlightHeader(
    title: String,
    date: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = date,
            fontSize = 12.sp,
            color = androidx.compose.ui.graphics.Color.Gray
        )
    }
}

@Composable
private fun HighlightContent(content: String) {
    Text(
        text = content,
        fontSize = 14.sp,
        color = androidx.compose.ui.graphics.Color.LightGray
    )
}

@Composable
private fun HighlightActions(onShareClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share"
            )
        }
    }
}
