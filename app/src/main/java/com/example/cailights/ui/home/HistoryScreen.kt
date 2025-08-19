package com.example.cailights.ui.history
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cailights.data.model.User
import com.example.cailights.ui.history.components.*
// Keep HistoryTopBar inline to avoid cross-package import issues
import com.example.cailights.ui.home.components.FoodForThoughtSection
import com.example.cailights.ui.shared.components.HighlightCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onAddHighlightClick: () -> Unit,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onUserClick: (User) -> Unit = {},
    onFollowClick: (User) -> Unit = {},
    onSearchToggled: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLatestClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
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
        containerColor = MaterialTheme.colorScheme.background,
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
            onHighlightClick = onHighlightClick,
            onHighlightLongClick = onHighlightLongClick,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
 * Main content area - switches between search results and highlights list
 */
@Composable
private fun HistoryContent(
    state: HistoryState,
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
        HighlightsListView(
            highlights = state.highlights,
            onHighlightClick = onHighlightClick,
            onHighlightLongClick = onHighlightLongClick,
            modifier = modifier
        )
    }
}

/**
 * Highlights list view using unified HighlightCard components
 */
@Composable
private fun HighlightsListView(
    highlights: List<HighlightItem>,
    onHighlightClick: (HighlightItem) -> Unit,
    onHighlightLongClick: (HighlightItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(highlights, key = { it.id }) { highlight ->
            HighlightCard(
                highlight = highlight,
                onClick = { onHighlightClick(highlight) },
                modifier = Modifier.fillMaxWidth()
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
    onUserClick: (User) -> Unit = {},
    onFollowClick: (User) -> Unit = {},
    onNavigateToFoodForThought: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Content area
        if (state.isSearching) {
            SearchResultsList(
                results = state.searchResults,
                searchQuery = state.searchQuery,
                onHighlightClick = onHighlightClick,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FoodForThoughtSection(
                        onNavigateToFullScreen = onNavigateToFoodForThought
                    )
                }
                
                // Suggested users section - positioned right below the streak section (which is part of FoodForThoughtSection)
                if (state.suggestedUsers.isNotEmpty()) {
                    item {
                        SuggestedUsersSection(
                            users = state.suggestedUsers,
                            onUserClick = onUserClick,
                            onFollowClick = onFollowClick
                        )
                    }
                }
                
                items(state.highlights, key = { it.id }) { highlight ->
                    HighlightCard(
                        highlight = highlight,
                        onClick = { onHighlightClick(highlight) },
                        modifier = Modifier.fillMaxWidth()
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

@Composable
private fun SuggestedUsersSection(
    users: List<User>,
    onUserClick: (User) -> Unit,
    onFollowClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Suggested for you",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(users, key = { it.id }) { user ->
                val onUserClickCallback = remember(user, onUserClick) { { onUserClick(user) } }
                val onFollowClickCallback = remember(user, onFollowClick) { { onFollowClick(user) } }
                SuggestedUserCard(
                    user = user,
                    onUserClick = onUserClickCallback,
                    onFollowClick = onFollowClickCallback
                )
            }
        }
    }
}

@Composable
private fun SuggestedUserCard(
    user: User,
    onUserClick: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onUserClick,
        modifier = modifier.width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // User avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.firstOrNull()?.toString()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // User info
            Text(
                text = user.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            // Follow button
            Button(
                onClick = onFollowClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                colors = if (user.isFollowing) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    ButtonDefaults.buttonColors()
                },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (user.isFollowing) "Following" else "Follow",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
