package com.example.cailights.ui.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.cailights.ui.history.utils.HighlightProcessor

class HistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        loadHighlights()
    }

    // Public interface methods
    fun onHighlightClicked(highlight: HighlightItem) {
        _state.update { it.copy(selectedHighlight = highlight) }
    }

    fun onBottomSheetDismissed() {
        _state.update { it.copy(selectedHighlight = null) }
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

    private fun performSearch(query: String) {
        val results = HighlightProcessor.searchHighlights(_state.value.highlights, query)
        _state.update { it.copy(searchResults = results) }
    }

    private fun createSampleHighlights(): List<HighlightItem> {
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
}