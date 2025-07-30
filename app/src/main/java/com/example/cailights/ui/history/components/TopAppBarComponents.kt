package com.example.cailights.ui.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Immediate focus without LaunchedEffect for instant response
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }

    TopAppBar(
        title = {
            OptimizedSearchField(
                query = query,
                onQueryChanged = onQueryChanged,
                focusRequester = focusRequester,
                onSearchAction = { keyboardController?.hide() }
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close search",
                    tint = Color(0xFF000000)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun OptimizedSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    onSearchAction: () -> Unit
) {
    // Pre-calculated constants to avoid recalculation
    val searchFieldModifier = remember {
        Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Color(0xFFF5F5F5),
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 16.dp)
    }
    
    val textStyle = remember {
        TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF1C1C1E),
            fontWeight = FontWeight.Normal
        )
    }
    
    val placeholderStyle = remember {
        TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF9E9E9E),
            fontWeight = FontWeight.Normal
        )
    }

    Row(
        modifier = searchFieldModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pre-sized search icon
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null, // Remove accessibility for performance
            tint = Color(0xFF9E9E9E),
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Optimized BasicTextField
        BasicTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = textStyle,
            singleLine = true,
            cursorBrush = SolidColor(Color(0xFF4A4AFF)),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search highlights...",
                            style = placeholderStyle
                        )
                    }
                    innerTextField()
                }
            }
        )
        
        // Conditional clear button with minimal recomposition
        if (query.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { onQueryChanged("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null, // Remove for performance
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    onSearchClicked: () -> Unit,
    onAddHighlightClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                "Cailights",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000)
            )
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF000000)
                )
            }
            IconButton(onClick = onAddHighlightClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Highlight",
                    tint = Color(0xFF000000)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
