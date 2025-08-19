package com.example.cailights.ui.history

import com.example.cailights.data.model.User

// in HistoryViewModel.kt
data class HighlightItem(
    val id: Int,
    val date: String, // Display date (e.g., "26 July")
    val title: String,
    val fullContent: String,
    val tag: String = "", // Category/tag for filtering (e.g., "bootcamp", "lesson-learned", "newAchievement")
    val createdAt: Long = System.currentTimeMillis(), // Timestamp for ordering and "X mins ago"
    val username: String = "You", // Username of creator, default to "You" for current user
    val userId: String = "current_user" // User ID for distinguishing users
)

data class HistoryState(
    val highlights: List<HighlightItem> = emptyList(),
    val selectedHighlight: HighlightItem? = null, // To track the selected item for the bottom sheet
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<HighlightItem> = emptyList(),
    val suggestedUsers: List<User> = emptyList()
)