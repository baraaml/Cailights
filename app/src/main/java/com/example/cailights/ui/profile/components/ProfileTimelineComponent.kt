package com.example.cailights.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.shared.components.HighlightCard

@Composable
fun ProfileTimelineComponent(
    historyHighlights: List<HighlightItem>,
    onHighlightClick: ((HighlightItem) -> Unit)? = null,
    onHighlightLongClick: ((HighlightItem) -> Unit)? = null
) {
    // Use unified HighlightCard components for consistency
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(historyHighlights, key = { it.id }) { highlight ->
            HighlightCard(
                highlight = highlight,
                onClick = { onHighlightClick?.invoke(highlight) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
