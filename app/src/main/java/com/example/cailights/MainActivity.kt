package com.example.cailights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cailights.ui.addhighlight.AddHighlightScreen
import com.example.cailights.ui.addhighlight.AddHighlightViewModel
import com.example.cailights.ui.history.HistoryViewModel
import com.example.cailights.ui.history.components.BottomNavigation
import com.example.cailights.ui.profile.ProfileViewModel
import com.example.cailights.ui.profile.ProfileScreenContent
import com.example.cailights.ui.theme.CailightsTheme
import com.example.cailights.ui.history.HistoryScreenContent

class MainActivity : ComponentActivity() {

    // Pre-initialize ViewModels for faster navigation
    private val historyViewModel: HistoryViewModel by viewModels()
    private val addHighlightViewModel: AddHighlightViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CailightsTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                
                // Pager state for swipe navigation between main screens
                val pagerState = rememberPagerState(
                    initialPage = 0, // Start with History (index 0)
                    pageCount = { 3 } // History(0), Latest(1), Profile(2)
                )

                // WhatsApp-style slide navigation transitions
                NavHost(
                    navController = navController, 
                    startDestination = "main_pages",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        )
                    }
                ) {

                    // Main pages with swipe navigation
                    composable("main_pages") {
                        Scaffold(
                            topBar = {
                                // Hide top bar for profile page (page 2)
                                if (pagerState.currentPage != 2) {
                                    TopAppBar(
                                        title = { 
                                            val historyState by historyViewModel.state.collectAsState()
                                            if (pagerState.currentPage == 0 && historyState.isSearching) {
                                                com.example.cailights.ui.history.components.SearchAppBar(
                                                    query = historyState.searchQuery,
                                                    onQueryChanged = historyViewModel::onSearchQueryChanged,
                                                    onCloseClicked = historyViewModel::onSearchToggled
                                                )
                                            } else {
                                                Text(
                                                    when (pagerState.currentPage) {
                                                        0 -> "Cailights"
                                                        1 -> "Latest"
                                                        else -> "Cailights"
                                                    },
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        },
                                        actions = {
                                            // Only show actions for History page
                                            if (pagerState.currentPage == 0) {
                                                val historyState by historyViewModel.state.collectAsState()
                                                if (!historyState.isSearching) {
                                                    IconButton(onClick = historyViewModel::onSearchToggled) {
                                                        Icon(Icons.Default.Search, contentDescription = "Search")
                                                    }
                                                    IconButton(onClick = {
                                                        navController.navigate("add_highlight") {
                                                            launchSingleTop = true
                                                        }
                                                    }) {
                                                        Icon(Icons.Default.Add, contentDescription = "Add Highlight")
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            },
                            bottomBar = {
                                BottomNavigation(
                                    onHomeClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(0)
                                        }
                                    },
                                    onLatestClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(1)
                                        }
                                    },
                                    onProfileClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(2)
                                        }
                                    },
                                    currentRoute = when (pagerState.currentPage) {
                                        0 -> "history"
                                        1 -> "latest"
                                        2 -> "profile"
                                        else -> "history"
                                    }
                                )
                            }
                        ) { paddingValues ->
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                when (page) {
                                    0 -> {
                                        // History Screen Content (with Scaffold padding)
                                        val state by historyViewModel.state.collectAsState()
                                        HistoryScreenContent(
                                            state = state,
                                            onHighlightClick = historyViewModel::onHighlightClicked,
                                            onHighlightLongClick = { /* TODO: Handle long click */ },
                                            onDismissBottomSheet = historyViewModel::onBottomSheetDismissed,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(paddingValues)
                                        )
                                    }
                                    1 -> {
                                        // Latest Screen Content (with Scaffold padding)
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
                                            Button(onClick = {
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(0)
                                                }
                                            }) {
                                                Text("Go to Home")
                                            }
                                        }
                                    }
                                    2 -> {
                                        // Profile Screen Content (full screen, no padding)
                                        val state by profileViewModel.state.collectAsState()
                                        ProfileScreenContent(
                                            state = state,
                                            onHighlightClick = profileViewModel::onHighlightClicked,
                                            onHighlightLongClick = { /* TODO: Handle long click */ },
                                            onDismissBottomSheet = profileViewModel::onBottomSheetDismissed,
                                            onSearchToggled = profileViewModel::onSearchToggled,
                                            onSearchQueryChanged = profileViewModel::onSearchQueryChanged,
                                            onFilterClick = profileViewModel::onFilterClicked,
                                            onYearSelected = profileViewModel::onYearSelected,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
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
                }
            }
        }
    }
}




