package com.example.cailights.ui.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.cailights.ui.history.components.DefaultTopAppBar
import com.example.cailights.ui.history.components.SearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopBar(
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