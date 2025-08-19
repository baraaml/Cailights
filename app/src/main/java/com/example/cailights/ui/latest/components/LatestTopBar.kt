package com.example.cailights.ui.latest.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import com.example.cailights.ui.history.components.SearchAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestTopBar(
    isSearching: Boolean,
    searchQuery: String,
    onSearchToggled: () -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    if (isSearching) {
        SearchAppBar(
            query = searchQuery,
            onQueryChanged = onSearchQueryChanged,
            onCloseClicked = onSearchToggled
        )
    } else {
        TopAppBar(
            title = { Text("Latest", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            actions = {
                IconButton(onClick = onSearchToggled) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        )
    }
}
