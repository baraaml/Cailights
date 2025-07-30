package com.example.cailights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cailights.ui.addhighlight.AddHighlightScreen
import com.example.cailights.ui.addhighlight.AddHighlightViewModel
import com.example.cailights.ui.history.HistoryViewModel
import com.example.cailights.ui.theme.CailightsTheme
import com.example.cailights.ui.history.HistoryScreen

class MainActivity : ComponentActivity() {

    // Pre-initialize ViewModels for faster navigation
    private val historyViewModel: HistoryViewModel by viewModels()
    private val addHighlightViewModel: AddHighlightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CailightsTheme {
                val navController = rememberNavController()

                // Fast navigation with instant transitions
                NavHost(
                    navController = navController, 
                    startDestination = "history",
                    enterTransition = { fadeIn(animationSpec = tween(0)) }, // Instant
                    exitTransition = { fadeOut(animationSpec = tween(0)) }   // Instant
                ) {

                    composable("history") {
                        // Use pre-initialized ViewModel for instant access
                        val state by historyViewModel.state.collectAsState()

                        HistoryScreen(
                            state = state,
                            onAddHighlightClick = {
                                // Instant navigation - no animation delay
                                navController.navigate("add_highlight") {
                                    launchSingleTop = true
                                }
                            },
                            onHighlightClick = historyViewModel::onHighlightClicked,
                            onDismissBottomSheet = historyViewModel::onBottomSheetDismissed,
                            onSearchToggled = historyViewModel::onSearchToggled,
                            onSearchQueryChanged = historyViewModel::onSearchQueryChanged,
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
                        // Use pre-initialized ViewModel for instant screen load
                        val state by addHighlightViewModel.state.collectAsState()
                        
                        AddHighlightScreen(
                            state = state,
                            onNavigateBack = { 
                                // Fast back navigation
                                navController.popBackStack() 
                            },
                            onAchievementTextChanged = addHighlightViewModel::onAchievementTextChanged,
                            onTagTextChanged = addHighlightViewModel::onTagTextChanged,
                            onSaveClick = {
                                addHighlightViewModel.saveHighlight()
                                // Instant back navigation after save
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
