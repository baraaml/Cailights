// in app/src/main/java/com/hazem/cailights/ui/addhighlight/AddHighlightScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.addhighlight.AddHighlightState
import com.example.cailights.ui.theme.CailightsTheme

// We update the function signature to match MainActivity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHighlightScreen(
    state: AddHighlightState,
    onAchievementTextChanged: (String) -> Unit,
    onTagTextChanged: (String) -> Unit,
    onDateTextChanged: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    // Scaffold provides a basic layout structure with a TopBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Highlight", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background, // Match our dark theme
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from the scaffold
                .padding(horizontal = 16.dp), // Add our own horizontal padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Space from the top bar

            // Date Field (for now, it just displays text)
            // Column arranges elements vertically
            OutlinedTextField(
                value = state.selectedDate,
                onValueChange = {onDateTextChanged}, // Not changeable by typing
                readOnly = true, // User cannot type here
                label = { Text("Date") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Select Date"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tag Field
            OutlinedTextField(
                value = state.selectedTag,
                onValueChange = onTagTextChanged, // Connect to the ViewModel function
                label = { Text("Pick a tag") },
                trailingIcon = {
                    Text("#", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Achievement Text Area
            OutlinedTextField(
                value = state.achievementText,
                onValueChange = onAchievementTextChanged, // Connect to the ViewModel function
                label = { Text("What did you achieve?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Make it taller
            )

            Spacer(modifier = Modifier.weight(1f)) // This pushes the button to the bottom

            // Save Button
            Button(
                onClick = onSaveClick, // Connect to the ViewModel function
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }
    }
}


@Preview(name = "Empty State", showBackground = true)
@Composable
private fun AddHighlightScreenPreview_Empty() {
    // A preview function needs to wrap your actual screen in your app's theme
    // to make sure it gets the right colors, fonts, etc.
    CailightsTheme {
        AddHighlightScreen(
            // For a preview, we create a fake state object on the fly
            state = AddHighlightState(),

            // The event handlers can just be empty lambdas, since we can't
            // interact with the ViewModel in a preview.
            onAchievementTextChanged = {},
            onTagTextChanged = {},
            onSaveClick = {},
            onDateTextChanged = {},
            onNavigateBack = { }
        )
    }
}