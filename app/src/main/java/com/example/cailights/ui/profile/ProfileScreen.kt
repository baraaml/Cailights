package com.example.cailights.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.history.components.BottomNavigation
import com.example.cailights.ui.history.components.HighlightDetailCard
import com.example.cailights.ui.profile.components.ActivityGraphComponent
import com.example.cailights.ui.profile.components.ProfileActionBarComponent
import com.example.cailights.ui.profile.components.ProfileHeaderComponent
import com.example.cailights.ui.profile.components.ProfilePhotoComponent
import com.example.cailights.ui.profile.components.ProfileTimelineComponent
import com.example.cailights.ui.profile.components.YearSelectorComponent
import com.example.cailights.ui.history.components.SearchResultsList
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.zIndex


/**
 * Content-only version of ProfileScreen for use within paged navigation
 * This version excludes Scaffold to avoid nested scaffolds
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onSearchToggled: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onYearSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isSearching) {
            // When searching, show search results in a full-screen layout
            SearchResultsList(
                results = state.searchResults,
                searchQuery = state.searchQuery,
                onHighlightClick = onHighlightClick,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 64.dp) // Leave space for search bar
            )
        } else {
            // When not searching, show normal profile content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "profile_photo") {
                    ProfilePhotoComponent(
                        userName = state.userName,
                        userPhotoUrl = state.userPhotoUrl
                    )
                }
                
                item(key = "action_bar") {
                    ProfileActionBarComponent(
                        currentYear = state.currentYear,
                        onSearchClick = onSearchToggled,
                        onFilterClick = onFilterClick,
                        onYearClick = { onYearSelected(state.currentYear) }
                    )
                }
                
                item(key = "divider") {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
                
                item(key = "timeline") {
                    ProfileTimelineComponent(
                        historyHighlights = state.historyHighlights,
                        onHighlightClick = onHighlightClick,
                        onHighlightLongClick = onHighlightLongClick
                    )
                }
                
                // Bottom spacing for comfortable scrolling
                item(key = "bottom_spacer") { 
                    Spacer(modifier = Modifier.height(24.dp)) 
                }
            }
        }
        
        // Floating search overlay when search is active
        if (state.isSearching) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .zIndex(1f)
            ) {
                com.example.cailights.ui.history.components.SearchAppBar(
                    query = state.searchQuery,
                    onQueryChanged = onSearchQueryChanged,
                    onCloseClicked = onSearchToggled
                )
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
