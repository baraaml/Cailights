package com.example.cailights.ui.history.utils

import com.example.cailights.ui.history.HighlightItem

object HighlightProcessor {

    /**
     * Search functionality for highlights
     */
    fun searchHighlights(highlights: List<HighlightItem>, query: String): List<HighlightItem> {
        if (query.isBlank()) return emptyList()

        val lowerQuery = query.lowercase()
        return highlights.filter { highlight ->
            highlight.title.lowercase().contains(lowerQuery) ||
                    highlight.fullContent.lowercase().contains(lowerQuery) ||
                    highlight.date.lowercase().contains(lowerQuery)
        }
    }
}