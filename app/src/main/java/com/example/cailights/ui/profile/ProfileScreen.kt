package com.example.cailights.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.ui.history.components.HighlightDetailCard
import com.example.cailights.ui.history.components.SearchAppBar
import com.example.cailights.ui.history.components.SearchResultsList
import com.example.cailights.ui.profile.components.ProfilePhotoComponent
import com.example.cailights.ui.shared.components.HighlightCard


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
    onSavedClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isSearching) {
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                SearchAppBar(
                    query = state.searchQuery,
                    onQueryChanged = onSearchQueryChanged,
                    onCloseClicked = onSearchToggled
                )
                SearchResultsList(
                    results = state.searchResults,
                    searchQuery = state.searchQuery,
                    onHighlightClick = onHighlightClick,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Profile header (photo + name)
                item(key = "profile_photo") {
                    ProfilePhotoComponent(
                        userName = state.userName,
                        userPhotoUrl = state.userPhotoUrl
                    )
                }

                // Quick actions row (Saved, Search)
                item(key = "quick_actions") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onSavedClick) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved Highlights",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = onSearchToggled) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                item(key = "divider") {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }

                // Pinned section
                if (state.pinnedAchievement != null) {
                    item(key = "pinned_header") {
                        Text(
                            text = "\uD83D\uDCCC Pinned",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    item(key = "pinned_card") {
                        val pinned = state.pinnedAchievement
                        val pinnedHighlight = HighlightItem(
                            id = -1,
                            date = pinned!!.date,
                            title = pinned.title,
                            fullContent = pinned.description,
                            tag = "",
                            username = "You",
                            userId = "current_user"
                        )
                        HighlightCard(
                            highlight = pinnedHighlight,
                            onClick = { onHighlightClick(pinnedHighlight) },
                            showPinIndicator = true,
                            showSaveButton = false
                        )
                    }
                }

                // Recent Highlights
                if (state.historyHighlights.isNotEmpty()) {
                    item(key = "recent_header") {
                        Text(
                            text = "Recent Highlights",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    items(state.historyHighlights, key = { it.id }) { highlight ->
                        HighlightCard(
                            highlight = highlight,
                            onClick = { onHighlightClick(highlight) },
                            showSaveButton = false
                        )
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Bottom sheet for highlight detail
        if (state.selectedHighlight != null) {
            ModalBottomSheet(
                onDismissRequest = onDismissBottomSheet,
                sheetState = rememberModalBottomSheetState()
            ) {
                HighlightDetailCard(
                    highlight = state.selectedHighlight,
                    onShareClick = {}
                )
            }
        }
    }
}