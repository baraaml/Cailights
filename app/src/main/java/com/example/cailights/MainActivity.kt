package com.example.cailights

import AddHighlightScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cailights.ui.addhighlight.AddHighlightViewModel
import com.example.cailights.ui.history.HistoryViewModel
import com.example.cailights.ui.theme.CailightsTheme
import com.example.cailights.ui.history.HistoryScreen

class MainActivity : ComponentActivity() {

    // You don't need a ViewModel instance here for the whole activity.
    // Each screen will create its own instance, which is cleaner.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CailightsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "history") {

                    composable("history") {
                        // Create a ViewModel instance specifically for this screen
                        val viewModel: HistoryViewModel by viewModels()
                        val state by viewModel.state.collectAsState()

                        HistoryScreen(
                            state = state,
                            onAddHighlightClick = {
                                navController.navigate("add_highlight")
                            },
                            // --- This is the new code ---
                            // When a highlight is clicked in the UI, call the ViewModel's function
                            onHighlightClick = viewModel::onHighlightClicked,
                            // When the sheet is dismissed, call the ViewModel's function
                            onDismissBottomSheet = viewModel::onBottomSheetDismissed,
                            onSearchToggled = viewModel::onSearchToggled,
                            onSearchQueryChanged = viewModel::onSearchQueryChanged,
                            onHomeClick = {
                                // Already on home, no action needed
                            },
                            onLatestClick = {
                                navController.navigate("latest")
                            },
                            onProfileClick = {
                                navController.navigate("profile")
                            }
                        )
                    }

                    composable("add_highlight") {
                        val viewModel: AddHighlightViewModel by viewModels()
                        val state by viewModel.state.collectAsState()
                        AddHighlightScreen(
                            state = state,
                            // Pass the navigation event to the screen
                            onNavigateBack = { navController.popBackStack() },
                            onAchievementTextChanged = viewModel::onAchievementTextChanged,
                            onTagTextChanged = viewModel::onTagTextChanged,
                            // Pass the date change event to the ViewModel
                            onDateTextChanged = viewModel::onDateTextChanged,
                            onSaveClick = {
                                viewModel.saveHighlight()
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("latest") {
                        LatestScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onHomeClick = { navController.navigate("history") }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onHomeClick = { navController.navigate("history") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestScreen(
    onNavigateBack: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Latest", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Latest Screen - Coming Soon!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onHomeClick) {
                Text("Go to Home")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Profile Screen - Coming Soon!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onHomeClick) {
                Text("Go to Home")
            }
        }
    }
}
