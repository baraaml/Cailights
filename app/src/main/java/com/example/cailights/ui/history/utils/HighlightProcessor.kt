package com.example.cailights.ui.history.utils

import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.history.components.ProcessedHighlight

/**
 * Utility object for processing highlight data for UI display
 */
object HighlightProcessor {
    
    /**
     * Process highlights for timeline display with alternating alignment
     */
    fun processHighlightsForTimeline(highlights: List<HighlightItem>): List<ProcessedHighlight> {
        return highlights.mapIndexed { index, item ->
            ProcessedHighlight(
                item = item,
                originalIndex = index,
                isLeftAligned = index % 2 == 0
            )
        }
    }
    
    /**
     * Search highlights based on query
     */
    fun searchHighlights(highlights: List<HighlightItem>, query: String): List<HighlightItem> {
        if (query.isBlank()) return emptyList()
        
        return highlights.filter { highlight ->
            highlight.title.contains(query, ignoreCase = true) ||
            highlight.fullContent.contains(query, ignoreCase = true)
        }
    }
}
