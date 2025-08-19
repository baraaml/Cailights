@file:OptIn(ExperimentalLayoutApi::class)

package com.example.cailights.ui.highlightdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.data.model.SavedCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightDetailScreen(
    viewModel: HighlightDetailViewModel,
    onNavigateBack: () -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HighlightDetailContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onUserClick = onUserClick,
        onSaveToggle = viewModel::toggleSave,
        onCategorySelected = viewModel::toggleCategory,
        onCreateCategory = viewModel::createCategory,
        onShowSaveDialog = viewModel::showSaveDialog,
        onDismissSaveDialog = viewModel::dismissSaveDialog,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightDetailContent(
    state: HighlightDetailState,
    onNavigateBack: () -> Unit,
    onUserClick: (String) -> Unit,
    onSaveToggle: () -> Unit,
    onCategorySelected: (SavedCategory) -> Unit,
    onCreateCategory: (String, String) -> Unit,
    onShowSaveDialog: () -> Unit,
    onDismissSaveDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Clean, minimal top bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                if (state.highlight != null) {
                    IconButton(onClick = onSaveToggle) {
                        Icon(
                            if (state.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (state.isSaved) "Unsave" else "Save",
                            tint = if (state.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { /* Share */ }) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = "Share"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            state.highlight?.let { highlight ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Simple user info
                    item {
                        SimpleUserHeader(
                            highlight = highlight,
                            onUserClick = { onUserClick(highlight.userId) }
                        )
                    }

                    // Clean content display
                    item {
                        SimpleHighlightContent(highlight = highlight)
                    }

                    // Minimal save status
                    if (state.isSaved && state.savedCategories.isNotEmpty()) {
                        item {
                            SimpleSavedSection(
                                categories = state.savedCategories,
                                onShowSaveDialog = onShowSaveDialog
                            )
                        }
                    }

                    // Clean action buttons
                    item {
                        SimpleActionButtons(
                            isSaved = state.isSaved,
                            onSaveClick = if (state.isSaved) onSaveToggle else onShowSaveDialog
                        )
                    }
                }
            }
        }

        // Simple save dialog
        if (state.showSaveDialog) {
            SimpleSaveDialog(
                categories = state.availableCategories,
                selectedCategories = state.selectedCategories,
                onCategorySelected = onCategorySelected,
                onCreateCategory = onCreateCategory,
                onDismiss = onDismissSaveDialog,
                onSave = {
                    onSaveToggle()
                    onDismissSaveDialog()
                }
            )
        }
    }
}

@Composable
private fun SimpleUserHeader(
    highlight: HighlightItem,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Simple circular avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = highlight.username.firstOrNull()?.toString()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = highlight.username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = highlight.date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SimpleHighlightContent(
    highlight: HighlightItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tag if present
        if (highlight.tag.isNotEmpty()) {
            Text(
                text = "#${highlight.tag}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Title
        Text(
            text = highlight.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            lineHeight = MaterialTheme.typography.headlineSmall.lineHeight * 1.3
        )

        // Full content
        if (highlight.fullContent.isNotEmpty()) {
            Text(
                text = highlight.fullContent,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SimpleSavedSection(
    categories: List<SavedCategory>,
    onShowSaveDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved in ${categories.size} ${if (categories.size == 1) "category" else "categories"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(onClick = onShowSaveDialog) {
                Text("Edit")
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SimpleActionButtons(
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            onClick = onSaveClick,
            modifier = Modifier.weight(1f),
            colors = if (isSaved) {
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            } else {
                ButtonDefaults.filledTonalButtonColors()
            }
        ) {
            Icon(
                if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isSaved) "Saved" else "Save")
        }

        OutlinedButton(
            onClick = { /* Share */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Outlined.Share,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share")
        }
    }
}

@Composable
private fun SimpleSaveDialog(
    categories: List<SavedCategory>,
    selectedCategories: Set<String>,
    onCategorySelected: (SavedCategory) -> Unit,
    onCreateCategory: (String, String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }
    var showCreateCategory by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (showCreateCategory) "New Category" else "Save to Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )

                if (showCreateCategory) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(categories) { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCategorySelected(category) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Checkbox(
                                    checked = selectedCategories.contains(category.id),
                                    onCheckedChange = { onCategorySelected(category) }
                                )

                                Text(
                                    text = category.name,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = { showCreateCategory = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add category")
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (showCreateCategory && newCategoryName.isNotBlank()) {
                                onCreateCategory(newCategoryName, "")
                                showCreateCategory = false
                                newCategoryName = ""
                            } else if (!showCreateCategory) {
                                onSave()
                            }
                        },
                        enabled = if (showCreateCategory) newCategoryName.isNotBlank() else selectedCategories.isNotEmpty()
                    ) {
                        Text(if (showCreateCategory) "Create" else "Save")
                    }
                }
            }
        }
    }
}