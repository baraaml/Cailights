package com.example.cailights.ui.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.cailights.data.model.User
import com.example.cailights.ui.history.utils.HighlightProcessor

class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        loadHighlights()
        loadSuggestedUsers()
    }

    // Public interface methods
    fun onHighlightClicked(highlight: HighlightItem) {
        _state.update { it.copy(selectedHighlight = highlight) }
    }

    fun onBottomSheetDismissed() {
        _state.update { it.copy(selectedHighlight = null) }
    }

    fun onUserClicked(user: User) {
        // Navigate to user profile - implement later
    }

    fun onFollowClicked(user: User) {
        _state.update { currentState ->
            val updatedUsers = currentState.suggestedUsers.map { existingUser ->
                if (existingUser.id == user.id) {
                    existingUser.copy(isFollowing = !existingUser.isFollowing)
                } else {
                    existingUser
                }
            }
            currentState.copy(suggestedUsers = updatedUsers)
        }
    }

    fun onSearchToggled() {
        _state.update { currentState ->
            val newSearching = !currentState.isSearching
            currentState.copy(
                isSearching = newSearching,
                searchQuery = if (!newSearching) "" else currentState.searchQuery,
                searchResults = if (!newSearching) emptyList() else currentState.searchResults
            )
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _state.update { it.copy(searchQuery = newQuery) }
        performSearch(newQuery)
    }

    // Private implementation methods
    private fun loadHighlights() {
        _state.update { 
            it.copy(highlights = createSampleHighlights()) 
        }
    }

    private fun loadSuggestedUsers() {
        _state.update { 
            it.copy(suggestedUsers = createSuggestedUsers()) 
        }
    }

    private fun performSearch(query: String) {
        val results = HighlightProcessor.searchHighlights(_state.value.highlights, query)
        _state.update { it.copy(searchResults = results) }
    }

    private fun createSampleHighlights(): List<HighlightItem> {
        val now = System.currentTimeMillis()
        val dayInMillis = 24 * 60 * 60 * 1000L
        
        return listOf(
            HighlightItem(1, "26 July", "Successfully presented Q3 project proposal to stakeholders", "Presented the project proposal to stakeholders. Received positive feedback and approval to proceed to the next phase.", "newAchievement", now - 11 * dayInMillis, "You", "current_user"),
            HighlightItem(2, "10 July", "Completed advanced Kotlin programming course with certification", "Finished the 'Advanced Asynchronous Programming in Kotlin' course, focusing on Coroutines and Flow.", "bootcamp", now - 27 * dayInMillis, "You", "current_user"),
            HighlightItem(3, "19 June", "Led refactoring of legacy login flow module", "Led the refactoring of the legacy login module, improving performance by 30% and reducing crashes.", "lesson-learned", now - 48 * dayInMillis, "You", "current_user"),
            HighlightItem(4, "05 June", "Started mentoring new junior developer team member", "Started mentoring a new team member, helping them set up their environment and understand the codebase.", "newAchievement", now - 62 * dayInMillis, "You", "current_user"),
            HighlightItem(5, "26 May", "Deployed new feature set to production environment", "Successfully deployed the new feature set to production, resulting in a 15% increase in user engagement.", "lesson-learned", now - 72 * dayInMillis, "You", "current_user"),
            HighlightItem(6, "17 May", "Had productive meeting with key technical recruiter", "He reviewed my CV and gave important notes, which made things easier when applying to other jobs.", "bootcamp", now - 81 * dayInMillis, "You", "current_user"),
            HighlightItem(7, "20 April", "Received exceeds expectations performance review rating", "Received an 'Exceeds Expectations' rating in my semi-annual performance review, with specific praise for my technical contributions.", "newAchievement", now - 108 * dayInMillis, "You", "current_user"),
            HighlightItem(8, "08 April", "Published technical blog post about Jetpack Compose", "Published a post on Medium about managing state in Jetpack Compose, which was featured in a popular newsletter.", "lesson-learned", now - 120 * dayInMillis, "You", "current_user"),
            HighlightItem(9, "25 March", "Solved critical production bug during on-call rotation", "Identified and fixed a critical bug under pressure during an on-call rotation, preventing further user impact.", "lesson-learned", now - 135 * dayInMillis, "You", "current_user"),
            HighlightItem(10, "11 March", "Made successful contribution to popular open-source library", "My pull request to a popular open-source library was reviewed and merged.", "newAchievement", now - 149 * dayInMillis, "You", "current_user"),
            HighlightItem(11, "28 Feb", "Delivered internal tech talk about design systems", "Presented a talk to the mobile development team about the benefits of using a design system.", "bootcamp", now - 160 * dayInMillis, "You", "current_user"),
            HighlightItem(12, "14 Feb", "Conceived initial project idea and concept for Cailights", "Came up with the initial concept and feature list for the Cailights application.", "newAchievement", now - 174 * dayInMillis, "You", "current_user")
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