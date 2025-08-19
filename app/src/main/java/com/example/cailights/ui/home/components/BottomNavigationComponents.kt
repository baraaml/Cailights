package com.example.cailights.ui.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigation(
    onHomeClick: () -> Unit,
    onLatestClick: () -> Unit,
    onProfileClick: () -> Unit,
    currentRoute: String = "history",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
                // Top border line like GitHub mobile
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.height(56.dp),
            tonalElevation = 0.dp
        ) {
            NavigationBarItem(
                icon = { 
                    Icon(
                        Icons.Default.Home, 
                        contentDescription = "Home",
                        modifier = Modifier.size(25.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Home",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                selected = currentRoute == "history",
                onClick = onHomeClick,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                icon = { 
                    Icon(
                        Icons.Default.Refresh, 
                        contentDescription = "Latest",
                        modifier = Modifier.size(25.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Latest",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                selected = currentRoute == "latest",
                onClick = onLatestClick,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                icon = { 
                    Icon(
                        Icons.Default.Person, 
                        contentDescription = "Profile",
                        modifier = Modifier.size(25.dp)
                    ) 
                },
                label = { 
                    Text(
                        "Profile",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                selected = currentRoute == "profile",
                onClick = onProfileClick,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
