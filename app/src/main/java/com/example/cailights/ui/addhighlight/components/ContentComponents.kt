package com.example.cailights.ui.addhighlight.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cailights.ui.addhighlight.AddHighlightState

@Composable
fun AddHighlightContent(
    state: AddHighlightState,
    onAchievementTextChanged: (String) -> Unit,
    onTagTextChanged: (String) -> Unit,
    onDateClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date Field
        ModernDateField(
            value = state.selectedDate,
            onDateClick = onDateClick,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tag Field
        ModernTagField(
            value = state.selectedTag,
            onValueChange = onTagTextChanged,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Achievement Field
        ModernAchievementField(
            value = state.achievementText,
            onValueChange = onAchievementTextChanged,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Save Button
        ModernSaveButton(
            onClick = onSaveClick,
            isLoading = state.isSaving,
            enabled = state.achievementText.isNotBlank()
        )
    }
}
