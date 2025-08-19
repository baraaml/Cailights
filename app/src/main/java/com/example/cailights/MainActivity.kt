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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.cailights.ui.latest.LatestViewModel
import com.example.cailights.ui.latest.LatestScreen
import com.example.cailights.ui.profile.ProfileViewModel
import com.example.cailights.ui.profile.ProfileScreenContent
import com.example.cailights.ui.userprofile.UserProfileViewModel
import com.example.cailights.ui.userprofile.UserProfileScreen
import com.example.cailights.ui.highlightdetail.HighlightDetailViewModel
import com.example.cailights.ui.highlightdetail.HighlightDetailScreen
import com.example.cailights.ui.saved.SavedHighlightsViewModel
import com.example.cailights.ui.saved.SavedHighlightsScreen
import com.example.cailights.ui.foodforthought.EnhancedFoodForThoughtScreen
import com.example.cailights.ui.theme.CailightsTheme
import com.example.cailights.ui.history.HistoryScreenContent

class MainActivity : ComponentActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private val addHighlightViewModel: AddHighlightViewModel by viewModels()
    private val latestViewModel: LatestViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val highlightDetailViewModel: HighlightDetailViewModel by viewModels()
    private val savedHighlightsViewModel: SavedHighlightsViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CailightsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val coroutineScope = rememberCoroutineScope()
                
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { 3 }
                )

                val currentPage by remember { androidx.compose.runtime.derivedStateOf { pagerState.currentPage } }

                val navigateToAddHighlight = remember(navController) {
                    { navController.navigate("add_highlight") { launchSingleTop = true } }
                }
                val navigateToFoodForThought = remember(navController) {
                    { navController.navigate("food_for_thought") { launchSingleTop = true } }
                }
                val navigateToUserProfile = remember(navController) {
                    { userId: String ->
                        navController.navigate("user_profile/$userId") { launchSingleTop = true }
                    }
                }
                val navigateToHighlightDetail = remember(navController) {
                    { highlightId: Int, userId: String ->
                        navController.navigate("highlight_detail/$highlightId/$userId") { launchSingleTop = true }
                    }
                }

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

                    composable("main_pages") {
                        Scaffold(
                            containerColor = MaterialTheme.colorScheme.background,
                            topBar = {
                                if (currentPage == 0) {
                                    val historyState by historyViewModel.state.collectAsStateWithLifecycle()
                                    if (historyState.isSearching) {
                                        com.example.cailights.ui.history.components.SearchAppBar(
                                            query = historyState.searchQuery,
                                            onQueryChanged = historyViewModel::onSearchQueryChanged,
                                            onCloseClicked = historyViewModel::onSearchToggled
                                        )
                                    } else {
                                        TopAppBar(
                                            title = {
                                                Text(
                                                    "Cailights",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            actions = {
                                                IconButton(onClick = historyViewModel::onSearchToggled) {
                                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                                }
                                            }
                                        )
                                    }
                                } else if (currentPage == 1) {
                                    val latestSearch by latestViewModel.searchState.collectAsStateWithLifecycle()
                                    if (latestSearch.isSearching) {
                                        com.example.cailights.ui.history.components.SearchAppBar(
                                            query = latestSearch.searchQuery,
                                            onQueryChanged = latestViewModel::onSearchQueryChanged,
                                            onCloseClicked = latestViewModel::onSearchToggled
                                        )
                                    } else {
                                        TopAppBar(
                                            title = {
                                                Text(
                                                    "Latest",
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            actions = {
                                                IconButton(onClick = latestViewModel::onSearchToggled) {
                                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                                }
                                            }
                                        )
                                    }
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
                                    currentRoute = when (currentPage) {
                                        0 -> "history"
                                        1 -> "latest"
                                        2 -> "profile"
                                        else -> "history"
                                    }
                                )
                            },
                            floatingActionButton = {
                                if (currentPage == 0) {
                                    FloatingActionButton(
                                        onClick = navigateToAddHighlight,
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add Highlight",
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                        ) { paddingValues ->
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                when (page) {
                                    0 -> {
                                        val state by historyViewModel.state.collectAsStateWithLifecycle()
                                        HistoryScreenContent(
                                            state = state,
                                            onHighlightClick = { highlight -> navigateToHighlightDetail(highlight.id, highlight.userId) },
                                            onHighlightLongClick = { /* TODO: Handle long click */ },
                                            onDismissBottomSheet = historyViewModel::onBottomSheetDismissed,
                                            onUserClick = { user -> navigateToUserProfile(user.id) },
                                            onFollowClick = { user -> historyViewModel.onFollowClicked(user) },
                                            onNavigateToFoodForThought = navigateToFoodForThought,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(paddingValues)
                                        )
                                    }
                                    1 -> {
                                        LatestScreen(
                                            viewModel = latestViewModel,
                                            onUserClick = { userId -> navigateToUserProfile(userId) },
                                            onHighlightClick = { highlightId, userId -> navigateToHighlightDetail(highlightId, userId) },
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(paddingValues)
                                        )
                                    }
                                    2 -> {
                                        val state by profileViewModel.state.collectAsStateWithLifecycle()
                                        ProfileScreenContent(
                                            state = state,
                                            onHighlightClick = profileViewModel::onHighlightClicked,
                                            onHighlightLongClick = { /* TODO: Handle long click */ },
                                            onDismissBottomSheet = profileViewModel::onBottomSheetDismissed,
                                            onSearchToggled = profileViewModel::onSearchToggled,
                                            onSearchQueryChanged = profileViewModel::onSearchQueryChanged,
                                            onFilterClick = profileViewModel::onFilterClicked,
                                            onYearSelected = profileViewModel::onYearSelected,
                                            onSavedClick = {
                                                navController.navigate("saved_highlights") { launchSingleTop = true }
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    composable("add_highlight") {
                        val state by addHighlightViewModel.state.collectAsStateWithLifecycle()
                        
                        AddHighlightScreen(
                            state = state,
                            onNavigateBack = { 
                                navController.popBackStack() 
                            },
                            onAchievementTextChanged = addHighlightViewModel::onAchievementTextChanged,
                            onTagTextChanged = addHighlightViewModel::onTagTextChanged,
                            onSaveClick = {
                                addHighlightViewModel.saveHighlight()
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("user_profile/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        
                        LaunchedEffect(userId) {
                            userProfileViewModel.loadUserProfile(userId)
                        }
                        
                        UserProfileScreen(
                            viewModel = userProfileViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("highlight_detail/{highlightId}/{userId}") { backStackEntry ->
                        val highlightId = backStackEntry.arguments?.getString("highlightId")?.toIntOrNull() ?: 0
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        
                        LaunchedEffect(highlightId, userId) {
                            highlightDetailViewModel.loadHighlight(highlightId, userId)
                        }
                        
                        HighlightDetailScreen(
                            viewModel = highlightDetailViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onUserClick = { clickedUserId ->
                                navController.navigate("user_profile/$clickedUserId") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable("saved_highlights") {
                        SavedHighlightsScreen(
                            viewModel = savedHighlightsViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onHighlightClick = { highlightId, userId ->
                                navController.navigate("highlight_detail/$highlightId/$userId") {
                                    launchSingleTop = true
                                }
                            },
                            onUserClick = { userId ->
                                navController.navigate("user_profile/$userId") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    
                    composable("food_for_thought") {
                        EnhancedFoodForThoughtScreen()
                    }
                }
                }
            }
        }
    }
}




