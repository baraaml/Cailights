package com.example.cailights.ui.theme

import androidx.compose.ui.graphics.Color

// Light theme colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Cailights brand color palette (from your design)
val BrandDarkPurple = Color(0xFF0D0221)      // Darkest purple from palette
val BrandFederalBlue = Color(0xFF0F084B)     // Federal blue
val BrandMarianBlue = Color(0xFF26408B)      // Marian blue  
val BrandLightBlue = Color(0xFFA6CFD5)       // Light blue
val BrandMintGreen = Color(0xFFC2E7D9)       // Mint green

// Cailights custom colors for light theme
val CaiLightPrimary = BrandMarianBlue         // Primary blue (#26408B)
val CaiLightSecondary = BrandLightBlue        // Light blue accent (#A6CFD5)
val CaiLightTertiary = BrandMintGreen         // Mint green accent (#C2E7D9)
val CaiLightText = BrandDarkPurple            // Dark purple text (#0D0221)
val CaiLightTextSecondary = BrandFederalBlue.copy(alpha = 0.7f)  // Federal blue for secondary text
val CaiLightBackground = Color(0xFFFCFCFD)     // Light background
val CaiLightSurface = Color(0xFFFFFFFF)        // White surface
val CaiLightSurfaceVariant = BrandMintGreen.copy(alpha = 0.1f) // Very light mint green surface

// Cailights custom colors for dark theme
val CaiDarkPrimary = BrandLightBlue           // Lighter blue for dark mode (#A6CFD5)
val CaiDarkSecondary = BrandMintGreen         // Mint green accent (#C2E7D9)
val CaiDarkTertiary = BrandMarianBlue         // Blue accent (#26408B)
val CaiDarkText = Color(0xFFF1F5F9)           // Light text
val CaiDarkTextSecondary = BrandLightBlue.copy(alpha = 0.8f)   // Light blue for secondary text
val CaiDarkBackground = BrandDarkPurple       // Dark purple background (#0D0221)
val CaiDarkSurface = BrandFederalBlue         // Federal blue surface (#0F084B)
val CaiDarkSurfaceVariant = BrandFederalBlue.copy(alpha = 0.8f)  // Lighter federal blue surface