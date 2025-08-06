package com.example.cailights.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.history.utils.HighlightProcessor

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        _state.update { currentState ->
            currentState.copy(
                activityData = generateActivityData(),
                pinnedAchievement = createPinnedAchievement(),
                historyHighlights = loadHistoryHighlights(), // Load real history data
                timelineEvents = createTimelineEvents()
            )
        }
    }

    private fun loadHistoryHighlights(): List<HighlightItem> {
        // Load the same highlights that are shown in the history page
        return listOf(
            HighlightItem(1, "26 July", "Successfully presented Q3 project proposal to stakeholders", "Presented the project proposal to stakeholders. Received positive feedback and approval to proceed to the next phase."),
            HighlightItem(2, "10 July", "Completed advanced Kotlin programming course with certification", "Finished the 'Advanced Asynchronous Programming in Kotlin' course, focusing on Coroutines and Flow."),
            HighlightItem(3, "19 June", "Led refactoring of legacy login flow module", "Led the refactoring of the legacy login module, improving performance by 30% and reducing crashes."),
            HighlightItem(4, "05 June", "Started mentoring new junior developer team member", "Started mentoring a new team member, helping them set up their environment and understand the codebase."),
            HighlightItem(5, "26 May", "Deployed new feature set to production environment", "Successfully deployed the new feature set to production, resulting in a 15% increase in user engagement."),
            HighlightItem(6, "17 May", "Had productive meeting with key technical recruiter", "He reviewed my CV and gave important notes, which made things easier when applying to other jobs."),
            HighlightItem(7, "20 April", "Received exceeds expectations performance review rating", "Received an 'Exceeds Expectations' rating in my semi-annual performance review, with specific praise for my technical contributions."),
            HighlightItem(8, "08 April", "Published technical blog post about Jetpack Compose", "Published a post on Medium about managing state in Jetpack Compose, which was featured in a popular newsletter."),
            HighlightItem(9, "25 March", "Solved critical production bug during on-call rotation", "Identified and fixed a critical bug under pressure during an on-call rotation, preventing further user impact."),
            HighlightItem(10, "11 March", "Made successful contribution to popular open-source library", "My pull request to a popular open-source library was reviewed and merged."),
            HighlightItem(11, "28 Feb", "Delivered internal tech talk about design systems", "Presented a talk to the mobile development team about the benefits of using a design system."),
            HighlightItem(12, "14 Feb", "Conceived initial project idea and concept for Cailights", "Came up with the initial concept and feature list for the Cailights application.")
        )
    }

    private fun generateActivityData(): List<ActivityDay> {
        // Generate exactly 53 days to match GitHub-style contribution graph
        // 3 rows of 15 squares + 1 row of 8 squares = 53 total
        return (0..52).map { index ->
            val intensity = when {
                index in listOf(7, 15, 24, 32, 41, 48) -> 0.8f // High activity days
                index in listOf(3, 11, 19, 27, 35, 43, 51) -> 0.4f // Medium activity days
                index in listOf(1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 45, 49) -> 0.2f // Low activity days
                else -> 0.0f // No activity days
            }
            ActivityDay(dayIndex = index, intensity = intensity)
        }
    }

    private fun createPinnedAchievement(): PinnedAchievement {
        // Use one of the actual history highlights as the pinned achievement
        return PinnedAchievement(
            title = "Made another career\nachievement",
            description = "Published a post on Medium about managing state in Jetpack Compose, which was featured in a popular newsletter.",
            date = "June 15, 2025"
        )
    }

    private fun createTimelineEvents(): List<TimelineEvent> {
        return listOf(
            TimelineEvent(
                id = 1,
                date = "17 August",
                title = "Led refactoring\nof legacy code",
                isLeft = true
            ),
            TimelineEvent(
                id = 2,
                date = "26 July",
                title = "Started mentoring\nnew junior",
                isLeft = false
            ),
            TimelineEvent(
                id = 3,
                date = "6 July",
                title = "Unlocked another\nlevel of proficiency",
                isLeft = false
            )
        )
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

    fun onFilterClicked() {
        // TODO: Implement filter functionality
    }

    fun onYearSelected(year: Int) {
        _state.update { it.copy(currentYear = year) }
    }

    private fun performSearch(query: String) {
        val results = HighlightProcessor.searchHighlights(_state.value.historyHighlights, query)
        _state.update { it.copy(searchResults = results) }
    }

    fun onHighlightClicked(highlight: HighlightItem) {
        _state.update { it.copy(selectedHighlight = highlight, isBottomSheetVisible = true) }
    }

    fun onBottomSheetDismissed() {
        _state.update { it.copy(isBottomSheetVisible = false, selectedHighlight = null) }
    }
}
