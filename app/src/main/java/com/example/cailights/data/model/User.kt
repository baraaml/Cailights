package com.example.cailights.data.model

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val bio: String = "",
    val profileImageUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val highlightsCount: Int = 0,
    val isFollowing: Boolean = false,
    val isVerified: Boolean = false,
    val joinedDate: Long = System.currentTimeMillis()
)

data class UserProfile(
    val user: User,
    val highlights: List<com.example.cailights.ui.history.HighlightItem> = emptyList(),
    val pinnedHighlight: com.example.cailights.ui.history.HighlightItem? = null
)
