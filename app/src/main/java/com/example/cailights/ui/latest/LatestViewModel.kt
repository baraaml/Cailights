package com.example.cailights.ui.latest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.data.model.User

class LatestViewModel : ViewModel() {
    private val _state = MutableStateFlow(LatestState())
    val state = _state.asStateFlow()

    // Granular slices
    val headerState: StateFlow<LatestHeaderState> = state
        .map { s ->
            LatestHeaderState(
                topTags = s.topTags,
                suggestedUsers = s.suggestedUsers,
                selectedTag = s.selectedTag,
                isLoading = s.isLoading
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LatestHeaderState()
        )

    val feedState: StateFlow<LatestFeedState> = state
        .map { s -> LatestFeedState(feedHighlights = s.feedHighlights, savedHighlightIds = s.savedHighlightIds) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LatestFeedState()
        )

    val searchState: StateFlow<LatestSearchState> = state
        .map { s ->
            LatestSearchState(
                isSearching = s.isSearching,
                searchQuery = s.searchQuery,
                searchResults = s.searchResults,
                userResults = s.userResults
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LatestSearchState()
        )

    init {
        loadLatestData()
    }

    fun onSearchToggled() {
        _state.update { current ->
            val newSearching = !current.isSearching
            current.copy(
                isSearching = newSearching,
                searchQuery = if (!newSearching) "" else current.searchQuery,
                searchResults = if (!newSearching) emptyList() else current.searchResults,
                userResults = if (!newSearching) emptyList() else current.userResults
            )
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _state.update { it.copy(searchQuery = newQuery) }
        performSearch(newQuery)
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _state.update { it.copy(searchResults = emptyList(), userResults = emptyList()) }
            return
        }

        // Users: global search across app
        val allUsers = createSuggestedUsers() + createFollowedUsers()
        val matchingUsers = allUsers.distinctBy { it.id }.filter { u ->
            u.username.contains(query, true) || u.displayName.contains(query, true) || u.bio.contains(query, true)
        }

        // Highlights: only from followed users
        val followedIds = createFollowedUsers().map { it.id }.toSet()
        val base = filterHighlightsByTag(_state.value.selectedTag)
        val results = base.filter { h ->
            h.userId in followedIds && (
                h.title.contains(query, true) ||
                h.fullContent.contains(query, true) ||
                h.username.contains(query, true)
            )
        }

        _state.update { it.copy(searchResults = results, userResults = matchingUsers) }
    }

    // Fixed: Changed parameter type from String? to String? and updated logic
    fun onTagSelected(tagName: String?) {
        _state.update {
            it.copy(
                selectedTag = if (it.selectedTag == tagName) null else tagName,
                feedHighlights = filterHighlightsByTag(if (it.selectedTag == tagName) null else tagName)
            )
        }
    }

    fun followUser(userId: String) {
        _state.update { currentState ->
            currentState.copy(
                suggestedUsers = currentState.suggestedUsers.map { user ->
                    if (user.id == userId) {
                        user.copy(
                            isFollowing = !user.isFollowing,
                            followersCount = if (user.isFollowing) {
                                user.followersCount - 1
                            } else {
                                user.followersCount + 1
                            }
                        )
                    } else {
                        user
                    }
                }
            )
        }
    }

    fun saveHighlight(highlightId: Int, userId: String) {
        _state.update { current ->
            val newSet = current.savedHighlightIds.toMutableSet()
            if (newSet.contains(highlightId)) newSet.remove(highlightId) else newSet.add(highlightId)
            current.copy(savedHighlightIds = newSet)
        }
    }

    private fun loadLatestData() {
        val allHighlights = createSampleFeedHighlights()
        val topTags = calculateTopTags(allHighlights)
        val suggestedUsers = createSuggestedUsers()

        _state.update {
            it.copy(
                topTags = topTags,
                feedHighlights = allHighlights.sortedByDescending { highlight -> highlight.createdAt },
                suggestedUsers = suggestedUsers
            )
        }
    }

    private fun createFollowedUsers(): List<User> {
        // Simulated followed users set (could be sourced from repository)
        return listOf(
            User(
                id = "user_103",
                username = "alex_dev",
                displayName = "Alex Developer",
                bio = "Backend and systems",
                followersCount = 120,
                followingCount = 75,
                highlightsCount = 50,
                isFollowing = true,
                isVerified = false
            ),
            User(
                id = "user_105",
                username = "dev_mike",
                displayName = "Mike Johnson",
                bio = "Mobile dev",
                followersCount = 300,
                followingCount = 200,
                highlightsCount = 90,
                isFollowing = true
            )
        )
    }

    private fun calculateTopTags(highlights: List<HighlightItem>): List<TagInfo> {
        return highlights
            .groupBy { it.tag }
            .map { (tag, items) -> TagInfo(tag, items.size) }
            .sortedByDescending { it.count }
            .take(3)
    }

    private fun filterHighlightsByTag(tagName: String?): List<HighlightItem> {
        val allHighlights = createSampleFeedHighlights()
        return if (tagName == null) {
            allHighlights.sortedByDescending { it.createdAt }
        } else {
            allHighlights.filter { it.tag == tagName }.sortedByDescending { it.createdAt }
        }
    }

    private fun createSampleFeedHighlights(): List<HighlightItem> {
        val now = System.currentTimeMillis()
        val minuteInMillis = 60 * 1000L
        val hourInMillis = 60 * minuteInMillis
        val dayInMillis = 24 * hourInMillis

        return listOf(
            // Recent highlights from various users
            HighlightItem(101, "Today", "figured out how it's hard to keep wasting time in doing non important things.", "I realized that I've been spending too much time on activities that don't contribute to my goals. Need to be more intentional with my time.", "bootcamp", now - 5 * minuteInMillis, "gnanaraja", "user_101"),
            HighlightItem(102, "Today", "Made another career achievement", "Published technical blog post about Jetpack Compose, specially about state management which was featured in a popular newsletter.", "newAchievement", now - 61 * minuteInMillis, "amanda", "user_102"),
            HighlightItem(103, "Yesterday", "Completed advanced system design course", "Finished the distributed systems course and immediately applied the concepts to optimize our microservices architecture.", "lesson-learned", now - 4 * hourInMillis, "alex_dev", "user_103"),
            HighlightItem(104, "Yesterday", "Led my first team standup meeting", "Successfully facilitated the daily standup and helped the team identify blockers early. Felt confident in my leadership skills.", "newAchievement", now - 18 * hourInMillis, "sarah_codes", "user_104"),
            HighlightItem(105, "2 days ago", "Learned about React Native performance optimization", "Deep dive into React Native performance bottlenecks and how to use Flipper for debugging. Applied these techniques to reduce app startup time by 40%.", "bootcamp", now - 2 * dayInMillis, "dev_mike", "user_105"),
            HighlightItem(106, "3 days ago", "Fixed critical production bug under pressure", "Identified and resolved a memory leak that was causing crashes for 15% of users. The fix went out in a hotfix release within 2 hours.", "lesson-learned", now - 3 * dayInMillis, "bug_hunter", "user_106"),
            HighlightItem(107, "4 days ago", "Started contributing to open source project", "Made my first contribution to a popular UI library. The maintainers were very welcoming and provided great feedback on my PR.", "newAchievement", now - 4 * dayInMillis, "open_source_fan", "user_107"),
            HighlightItem(108, "1 week ago", "Attended advanced Kotlin workshop", "Learned about Kotlin coroutines flow operators and how to handle complex async operations. Already planning to refactor our current implementation.", "bootcamp", now - 7 * dayInMillis, "kotlin_lover", "user_108"),

            // Your own highlights mixed in
            HighlightItem(1, "26 July", "Successfully presented Q3 project proposal to stakeholders", "Presented the project proposal to stakeholders. Received positive feedback and approval to proceed to the next phase.", "newAchievement", now - 11 * dayInMillis, "You", "current_user"),
            HighlightItem(2, "10 July", "Completed advanced Kotlin programming course with certification", "Finished the 'Advanced Asynchronous Programming in Kotlin' course, focusing on Coroutines and Flow.", "bootcamp", now - 27 * dayInMillis, "You", "current_user"),
            HighlightItem(3, "19 June", "Led refactoring of legacy login flow module", "Led the refactoring of the legacy login module, improving performance by 30% and reducing crashes.", "lesson-learned", now - 48 * dayInMillis, "You", "current_user")
        )
    }

    private fun createSuggestedUsers(): List<User> {
        return listOf(
            User(
                id = "user_sarah",
                username = "sarah_dev",
                displayName = "Sarah Johnson",
                bio = "Senior Android Developer at TechCorp",
                followersCount = 247,
                followingCount = 89,
                highlightsCount = 34,
                isFollowing = false,
                isVerified = true
            ),
            User(
                id = "user_alex",
                username = "alex_codes",
                displayName = "Alex Chen",
                bio = "Full-stack developer • Open source contributor",
                followersCount = 156,
                followingCount = 203,
                highlightsCount = 28,
                isFollowing = false
            ),
            User(
                id = "user_priya",
                username = "priya_designs",
                displayName = "Priya Patel",
                bio = "UX/UI Designer • Design systems advocate",
                followersCount = 389,
                followingCount = 145,
                highlightsCount = 42,
                isFollowing = false
            )
        )
    }
}