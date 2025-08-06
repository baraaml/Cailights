package com.example.cailights.ui.profile

import com.example.cailights.ui.history.HighlightItem

data class ProfileState(
    val userName: String = "Baraa Ahmed",
    val currentYear: Int = 2025,
    val activityData: List<ActivityDay> = emptyList(),
    val pinnedAchievement: PinnedAchievement? = null,
    val historyHighlights: List<HighlightItem> = emptyList(), // Real history data
    val timelineEvents: List<TimelineEvent> = emptyList(), // Keep for backward compatibility
    val isLoading: Boolean = false,
    val selectedHighlight: HighlightItem? = null,
    val isBottomSheetVisible: Boolean = false,
    val userPhotoUrl: String = "", // Empty for now, can be set to actual URL later
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<HighlightItem> = emptyList()
)

data class ActivityDay(
    val dayIndex: Int,
    val intensity: Float // 0.0 to 1.0
)

data class PinnedAchievement(
    val title: String,
    val description: String,
    val date: String
)

data class TimelineEvent(
    val id: Int,
    val date: String,
    val title: String,
    val isLeft: Boolean
)
