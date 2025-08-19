package com.example.cailights.ui.theme

import androidx.compose.ui.graphics.Color

// Modern neutral colors for clean design
val NeutralWhite = Color(0xFFFFFFFF)
val NeutralGray50 = Color(0xFFFAFAFA)
val NeutralGray100 = Color(0xFFF5F5F5)
val NeutralGray200 = Color(0xFFEEEEEE)
val NeutralGray300 = Color(0xFFE0E0E0)
val NeutralGray400 = Color(0xFFBDBDBD)
val NeutralGray500 = Color(0xFF9E9E9E)
val NeutralGray600 = Color(0xFF757575)
val NeutralGray700 = Color(0xFF616161)
val NeutralGray800 = Color(0xFF424242)
val NeutralGray900 = Color(0xFF212121)
val NeutralBlack = Color(0xFF000000)

// Modern brand colors - Professional and clean
val BrandPrimary = Color(0xFF1976D2)        // Clean blue
val BrandPrimary50 = Color(0xFFE3F2FD)      // Very light blue
val BrandPrimary100 = Color(0xFFBBDEFB)     // Light blue
val BrandPrimary200 = Color(0xFF90CAF9)     // Medium light blue
val BrandPrimary300 = Color(0xFF64B5F6)     // Medium blue
val BrandPrimary400 = Color(0xFF42A5F5)     // Semi-strong blue
val BrandPrimary500 = Color(0xFF2196F3)     // Strong blue
val BrandPrimary600 = Color(0xFF1976D2)     // Primary blue
val BrandPrimary700 = Color(0xFF1565C0)     // Dark blue
val BrandPrimary800 = Color(0xFF0D47A1)     // Very dark blue
val BrandPrimary900 = Color(0xFF0A3D91)     // Darkest blue

// Success colors
val Success50 = Color(0xFFE8F5E8)
val Success100 = Color(0xFFC8E6C9)
val Success300 = Color(0xFF81C784)
val Success500 = Color(0xFF4CAF50)
val Success600 = Color(0xFF43A047)
val Success700 = Color(0xFF388E3C)

// Warning colors
val Warning50 = Color(0xFFFFF8E1)
val Warning100 = Color(0xFFFFECB3)
val Warning200 = Color(0xFFFFE082)
val Warning300 = Color(0xFFFFD54F)
val Warning400 = Color(0xFFFFCA28)
val Warning500 = Color(0xFFFFC107)
val Warning600 = Color(0xFFFFB300)
val Warning700 = Color(0xFFFFA000)

// Error colors
val Error50 = Color(0xFFFFEBEE)
val Error100 = Color(0xFFFFCDD2)
val Error300 = Color(0xFFEF5350)
val Error500 = Color(0xFFF44336)
val Error600 = Color(0xFFE53935)
val Error700 = Color(0xFFD32F2F)

// Information colors
val Info50 = Color(0xFFE1F5FE)
val Info100 = Color(0xFFB3E5FC)
val Info200 = Color(0xFF81D4FA)
val Info300 = Color(0xFF4FC3F7)
val Info400 = Color(0xFF29B6F6)
val Info500 = Color(0xFF03A9F4)
val Info600 = Color(0xFF039BE5)

// Purple accent for special features (Food for Thought)
val PurpleAccent50 = Color(0xFFF3E5F5)
val PurpleAccent100 = Color(0xFFE1BEE7)
val PurpleAccent200 = Color(0xFFCE93D8)
val PurpleAccent300 = Color(0xFFBA68C8)
val PurpleAccent400 = Color(0xFFAB47BC)
val PurpleAccent500 = Color(0xFF9C27B0)
val PurpleAccent600 = Color(0xFF8E24AA)
val PurpleAccent700 = Color(0xFF7B1FA2)

// Light Theme Color Scheme
val LightPrimary = BrandPrimary600
val LightOnPrimary = NeutralWhite
val LightPrimaryContainer = BrandPrimary50
val LightOnPrimaryContainer = BrandPrimary800

val LightSecondary = PurpleAccent500
val LightOnSecondary = NeutralWhite
val LightSecondaryContainer = PurpleAccent50
val LightOnSecondaryContainer = PurpleAccent700

val LightTertiary = Success600
val LightOnTertiary = NeutralWhite
val LightTertiaryContainer = Success50
val LightOnTertiaryContainer = Success700

val LightError = Error500
val LightOnError = NeutralWhite
val LightErrorContainer = Error50
val LightOnErrorContainer = Error600

val LightBackground = NeutralWhite
val LightOnBackground = NeutralGray900
val LightSurface = NeutralWhite
val LightOnSurface = NeutralGray900
val LightSurfaceVariant = NeutralGray50
val LightOnSurfaceVariant = NeutralGray600

val LightOutline = NeutralGray300
val LightOutlineVariant = NeutralGray200

// Dark Theme Color Scheme - Modern and clean
val DarkPrimary = BrandPrimary300
val DarkOnPrimary = BrandPrimary800
val DarkPrimaryContainer = BrandPrimary700
val DarkOnPrimaryContainer = BrandPrimary100

val DarkSecondary = PurpleAccent200
val DarkOnSecondary = PurpleAccent700
val DarkSecondaryContainer = PurpleAccent600
val DarkOnSecondaryContainer = PurpleAccent100

val DarkTertiary = Success300
val DarkOnTertiary = Success700
val DarkTertiaryContainer = Success600
val DarkOnTertiaryContainer = Success100

val DarkError = Error300
val DarkOnError = Error700
val DarkErrorContainer = Error600
val DarkOnErrorContainer = Error100

val DarkBackground = Color(0xFF0A0A0A)          // Very dark, almost black
val DarkOnBackground = Color(0xFFF5F5F5)        // Light text
val DarkSurface = Color(0xFF121212)             // Material Design dark surface
val DarkOnSurface = Color(0xFFF5F5F5)           // Light text on dark surface
val DarkSurfaceVariant = Color(0xFF1E1E1E)      // Slightly lighter surface
val DarkOnSurfaceVariant = Color(0xFFB3B3B3)    // Medium light text

val DarkOutline = Color(0xFF3A3A3A)             // Subtle outline
val DarkOutlineVariant = Color(0xFF2A2A2A)      // Very subtle outline

// Custom semantic colors for special features
val TechnicalColor = BrandPrimary500    // Blue for technical content
val CareerColor = Success600            // Green for career content
val LeadershipColor = Color(0xFFFF9800) // Orange for leadership content
val InnovationColor = Error500          // Red/pink for innovation content

// Achievement rarity colors (using existing palette)
val CommonColor = Success500            // Green for common achievements
val RareColor = BrandPrimary500         // Blue for rare achievements
val EpicColor = PurpleAccent500         // Purple for epic achievements
val LegendaryColor = Color(0xFFFFD700)  // Gold for legendary achievements

// Food for Thought gradient colors (dark theme compatible)
val FoodForThoughtDark1 = Color(0xFF1A0B2E)    // Deep purple
val FoodForThoughtDark2 = Color(0xFF16213E)    // Deep blue
val FoodForThoughtDark3 = Color(0xFF0F3460)    // Navy blue

// Legacy colors for backward compatibility
val Purple80 = PurpleAccent200
val PurpleGrey80 = NeutralGray300
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = PurpleAccent600
val PurpleGrey40 = NeutralGray600
val Pink40 = Color(0xFF7D5260)