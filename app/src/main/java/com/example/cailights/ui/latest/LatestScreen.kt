package com.example.cailights.ui.latest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.data.model.User
import com.example.cailights.ui.shared.components.HighlightCard
import com.example.cailights.ui.history.components.SearchAppBar
import com.example.cailights.ui.history.components.SearchResultsList

// Wrapper composable that takes ViewModel and handles state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestScreen(
    viewModel: LatestViewModel,
    onUserClick: (String) -> Unit = {},
    onHighlightClick: (Int, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val header by viewModel.headerState.collectAsStateWithLifecycle()
    val feed by viewModel.feedState.collectAsStateWithLifecycle()
    val search by viewModel.searchState.collectAsStateWithLifecycle()

    // Stabilize callbacks derived from ViewModel
    val stableOnTagSelected = remember(viewModel) { { tag: String? -> viewModel.onTagSelected(tag) } }

    LatestScreenContent(
        header = header,
        feed = feed,
        search = search,
        onTagSelected = stableOnTagSelected,
        onHighlightClick = onHighlightClick,
        onUserClick = onUserClick,
        onFollowUser = viewModel::followUser,
        onSaveHighlight = viewModel::saveHighlight,
        modifier = modifier
    )
}

// UI composable that takes state and callbacks
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestScreenContent(
    header: LatestHeaderState,
    feed: LatestFeedState,
    search: LatestSearchState,
    onTagSelected: (String?) -> Unit,
    onHighlightClick: (Int, String) -> Unit,
    onUserClick: (String) -> Unit,
    onFollowUser: (String) -> Unit,
    onSaveHighlight: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Remember derived immutable snapshots to minimize object identity changes
    val topTags = remember(header.topTags) { header.topTags }
    val feedHighlights = remember(feed.feedHighlights) { feed.feedHighlights }
    val savedIds = remember(feed.savedHighlightIds) { feed.savedHighlightIds }

    if (search.isSearching) {
        // Search results in a single scrollable list
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (search.userResults.isNotEmpty()) {
                item {
                    Text(
                        text = "People",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(items = search.userResults, key = { it.id }) { user ->
                    UserSearchCard(
                        user = user,
                        onUserClick = { onUserClick(user.id) },
                        onFollowClick = { onFollowUser(user.id) }
                    )
                }
            }

            if (search.searchResults.isNotEmpty()) {
                item {
                    Text(
                        text = "Highlights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = if (search.userResults.isNotEmpty()) 16.dp else 0.dp)
                    )
                }
                items(items = search.searchResults, key = { it.id }) { item ->
                    HighlightCard(
                        highlight = item,
                        onClick = { onHighlightClick(item.id, item.userId) },
                        onSaveClick = { onSaveHighlight(item.id, item.userId) },
                        onUserClick = { onUserClick(item.userId) }
                    )
                }
            }
        }
    } else {
        // Main content in a single scrollable list
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Top tags section
            item {
                TopTagsSection(
                    tags = topTags,
                    selectedTag = header.selectedTag,
                    onTagSelected = onTagSelected
                )
            }

            // Feed header
            item {
                Text(
                    text = "Feed",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Feed items
            items(items = feedHighlights, key = { it.id }) { highlight ->
                HighlightCard(
                    highlight = highlight,
                    onClick = { onHighlightClick(highlight.id, highlight.userId) },
                    isSaved = savedIds.contains(highlight.id),
                    onSaveClick = { onSaveHighlight(highlight.id, highlight.userId) },
                    onUserClick = { onUserClick(highlight.userId) }
                )
            }
        }
    }
}

@Composable
private fun TopTagsSection(
    tags: List<TagInfo>,
    selectedTag: String?,
    onTagSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Top tags",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(items = tags, key = { it.name }) { tag ->
                val onClick = remember(tag.name, onTagSelected) { { onTagSelected(tag.name) } }
                TagChip(
                    tag = tag,
                    isSelected = selectedTag == tag.name,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun TagChip(
    tag: TagInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Text(
            text = "#${tag.name}",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun UserSearchCard(
    user: User,
    onUserClick: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onUserClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.firstOrNull()?.toString()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // User info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Follow button
            Button(
                onClick = onFollowClick,
                colors = if (user.isFollowing) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(
                    text = if (user.isFollowing) "Following" else "Follow",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}