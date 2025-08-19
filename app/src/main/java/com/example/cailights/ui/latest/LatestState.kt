package com.example.cailights.ui.latest

import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.data.model.User
import androidx.compose.runtime.Immutable

@Immutable
data class TagInfo(
    val name: String,
    val count: Int
)

@Immutable
data class LatestState(
    val topTags: List<TagInfo> = emptyList(),
    val feedHighlights: List<HighlightItem> = emptyList(),
    val suggestedUsers: List<User> = emptyList(),
    val selectedTag: String? = null,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<HighlightItem> = emptyList(),
    val userResults: List<User> = emptyList(),
    val savedHighlightIds: Set<Int> = emptySet()
)

// Granular immutable state slices to minimize recompositions in Compose
@Immutable
data class LatestHeaderState(
    val topTags: List<TagInfo> = emptyList(),
    val suggestedUsers: List<User> = emptyList(),
    val selectedTag: String? = null,
    val isLoading: Boolean = false
)

@Immutable
data class LatestFeedState(
    val feedHighlights: List<HighlightItem> = emptyList(),
    val savedHighlightIds: Set<Int> = emptySet()
)

@Immutable
data class LatestSearchState(
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<HighlightItem> = emptyList(),
    val userResults: List<User> = emptyList()
)