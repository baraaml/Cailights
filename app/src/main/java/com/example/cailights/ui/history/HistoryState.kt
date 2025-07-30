package com.example.cailights.ui.history

// in HistoryViewModel.kt
data class HighlightItem(
    val id: Int,
    val date: String,
    val title: String,
    val fullContent: String
)

data class HistoryState(
    val highlights: List<HighlightItem> = emptyList(),
    val selectedHighlight: HighlightItem? = null, // To track the selected item for the bottom sheet
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<HighlightItem> = emptyList()
)