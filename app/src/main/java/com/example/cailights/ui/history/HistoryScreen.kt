package com.example.cailights.ui.history

import com.example.cailights.ui.shared.timeline.ProcessedHighlight
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.cailights.ui.history.components.*
import com.example.cailights.ui.history.utils.HighlightProcessor

/**
 * Main History Screen - Entry point for the history feature
 * Follows MVVM pattern with clear separation of concerns
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onAddHighlightClick: () -> Unit,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onSearchToggled: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLatestClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    // Process highlights for timeline display
    val processedHighlights = remember(state.highlights) {
        HighlightProcessor.processHighlightsForTimeline(state.highlights)
    }

    // Bottom sheet state management
    val sheetState = rememberModalBottomSheetState()

    // Show highlight detail bottom sheet when highlight is selected
    if (state.selectedHighlight != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissBottomSheet,
            sheetState = sheetState
        ) {
            HighlightDetailCard(
                highlight = state.selectedHighlight,
                onShareClick = {

                }
            )
        }
    }

    // Main scaffold structure
    Scaffold(
        topBar = {
            HistoryTopBar(
                isSearching = state.isSearching,
                searchQuery = state.searchQuery,
                onSearchToggled = onSearchToggled,
                onSearchQueryChanged = onSearchQueryChanged,
                onAddHighlightClick = onAddHighlightClick
            )
        },
        bottomBar = {
            BottomNavigation(
                onHomeClick = onHomeClick,
                onLatestClick = onLatestClick,
                onProfileClick = onProfileClick,
                currentRoute = "history"
            )
        }
    ) { paddingValues ->
        HistoryContent(
            state = state,
            processedHighlights = processedHighlights,
            onHighlightClick = onHighlightClick,
            onHighlightLongClick = onHighlightLongClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

/**
 * Manages the top app bar based on search state
 */
@Composable
private fun HistoryTopBar(
    isSearching: Boolean,
    searchQuery: String,
    onSearchToggled: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onAddHighlightClick: () -> Unit
) {
    if (isSearching) {
        SearchAppBar(
            query = searchQuery,
            onQueryChanged = onSearchQueryChanged,
            onCloseClicked = onSearchToggled
        )
    } else {
        DefaultTopAppBar(
            onSearchClicked = onSearchToggled,
            onAddHighlightClick = onAddHighlightClick
        )
    }
}

/**
 * Main content area - switches between search results and timeline
 */
@Composable
private fun HistoryContent(
    state: HistoryState,
    processedHighlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isSearching) {
        SearchResultsList(
            results = state.searchResults,
            searchQuery = state.searchQuery,
            onHighlightClick = onHighlightClick,
            modifier = modifier
        )
    } else {
        TimelineView(
            processedHighlights = processedHighlights,
            onHighlightClick = onHighlightClick,
            onHighlightLongClick = onHighlightLongClick,
            modifier = modifier
        )
    }
}

/**
 * Timeline view wrapped in LazyColumn
 */
@Composable
private fun TimelineView(
    processedHighlights: List<ProcessedHighlight>,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            TimelineSection(
                highlights = processedHighlights,
                onHighlightClick = onHighlightClick,
                onHighlightLongClick = onHighlightLongClick
            )
        }
    }
}

/**
 * Content-only version of HistoryScreen for use within paged navigation
 * This version excludes Scaffold to avoid nested scaffolds
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    state: HistoryState,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    onDismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Content area
        val processedHighlights = remember(state.highlights, state.searchQuery) {
            HighlightProcessor.processHighlightsForTimeline(state.highlights)
        }

        if (state.isSearching) {
            SearchResultsList(
                results = state.searchResults,
                searchQuery = state.searchQuery,
                onHighlightClick = onHighlightClick,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    TimelineSection(
                        highlights = processedHighlights,
                        onHighlightClick = onHighlightClick,
                        onHighlightLongClick = onHighlightLongClick
                    )
                }
            }
        }
    }
    
    // Show highlight detail bottom sheet when highlight is selected
    if (state.selectedHighlight != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissBottomSheet,
            sheetState = rememberModalBottomSheetState()
        ) {
            HighlightDetailCard(
                highlight = state.selectedHighlight,
                onShareClick = { }
            )
        }
    }
}
