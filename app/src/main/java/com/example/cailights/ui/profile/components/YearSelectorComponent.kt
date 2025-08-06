package com.example.cailights.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun YearSelectorComponent(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentYear.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { 
                // TODO: Implement year dropdown logic
                // This will trigger the dropdown menu
            }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select year")
            }
        }
        // TODO: Add DropdownMenu implementation when needed
        // Year dropdown will be added here when expanded
    }
}
