package com.example.cailights.ui.addhighlight

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cailights.ui.addhighlight.components.AddHighlightContent
import com.example.cailights.ui.addhighlight.components.AddHighlightTopBar
import com.example.cailights.ui.theme.CailightsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHighlightScreen(
    state: AddHighlightState,
    onAchievementTextChanged: (String) -> Unit,
    onTagTextChanged: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    // Fast navigation on success (no delay)
    if (state.isSuccess) {
        LaunchedEffect(state.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            AddHighlightTopBar(
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        AddHighlightContent(
            state = state,
            onAchievementTextChanged = onAchievementTextChanged,
            onTagTextChanged = onTagTextChanged,
            onDateClick = { /* TODO: Implement date picker */ },
            onSaveClick = onSaveClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview(name = "Empty State", showBackground = true)
@Composable
private fun AddHighlightScreenPreview_Empty() {
    CailightsTheme {
        AddHighlightScreen(
            state = AddHighlightState(),
            onAchievementTextChanged = {},
            onTagTextChanged = {},
            onSaveClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(name = "Filled State", showBackground = true)
@Composable
private fun AddHighlightScreenPreview_Filled() {
    CailightsTheme {
        AddHighlightScreen(
            state = AddHighlightState(
                achievementText = "Completed the app refactoring with modern MVVM architecture",
                selectedTag = "coding",
                selectedDate = "July 30, 2025"
            ),
            onAchievementTextChanged = {},
            onTagTextChanged = {},
            onSaveClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
private fun AddHighlightScreenPreview_Loading() {
    CailightsTheme {
        AddHighlightScreen(
            state = AddHighlightState(
                achievementText = "Completed the app refactoring",
                selectedTag = "coding",
                isSaving = true
            ),
            onAchievementTextChanged = {},
            onTagTextChanged = {},
            onSaveClick = {},
            onNavigateBack = {}
        )
    }
}