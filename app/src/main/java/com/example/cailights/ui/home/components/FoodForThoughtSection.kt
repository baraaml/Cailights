package com.example.cailights.ui.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cailights.ui.foodforthought.FoodForThoughtViewModel

@Composable
fun FoodForThoughtSection(
    modifier: Modifier = Modifier,
    onNavigateToFullScreen: () -> Unit = {}
) {
    val viewModel: FoodForThoughtViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Enhanced animations
    val infiniteTransition = rememberInfiniteTransition()
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val glowRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Enhanced header with animated title and achievement indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🧠",
                    fontSize = 24.sp,
                    modifier = Modifier.scale(1.1f + shimmer * 0.1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Food for thought",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // New achievement glow indicator
            if (state.recentAchievements.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(glowRotation)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700).copy(alpha = 0.6f),
                                    Color(0xFFFFD700).copy(alpha = 0.2f),
                                    Color.Transparent
                                ),
                                radius = 15f
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onNavigateToFullScreen() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "New Achievement",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        if (state.isLoading) {
            // Enhanced loading card
            LoadingQuestionCard(shimmerEffect = shimmer)
        } else {
            state.todaysQuestion?.let { question ->
                // Enhanced question card with dynamic gradients and better interactions
                EnhancedQuestionCard(
                    question = question,
                    hasAnsweredToday = state.hasAnsweredToday,
                    onAnswerClick = viewModel::showAnswerDialog,
                    onCardClick = onNavigateToFullScreen,
                    shimmerEffect = shimmer
                )
            } ?: run {
                // Fallback to original simple card if no question
                SimpleQuestionCard(onNavigateToFullScreen = onNavigateToFullScreen)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Enhanced stats row with real data
        state.currentStreak?.let { streak ->
            EnhancedStatsRow(
                currentStreak = streak.currentStreak,
                thisMonthResponses = streak.thisMonthResponses,
                hasNewAchievements = state.recentAchievements.isNotEmpty()
            )
        } ?: run {
            // Fallback stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    title = "Current Streak",
                    value = "12 days",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    title = "This Month",
                    value = "18 Logs",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
    
    // Enhanced answer dialog
    if (state.showAnswerDialog) {
        EnhancedAnswerDialog(
            question = state.todaysQuestion?.question ?: "",
            currentResponse = state.currentResponse,
            onResponseChange = viewModel::updateResponse,
            onSubmit = viewModel::submitResponse,
            onDismiss = viewModel::dismissAnswerDialog,
            isSubmitting = state.isLoading
        )
    }
    
    // Achievement celebration
    if (state.showAchievementDialog) {
        AchievementCelebrationDialog(
            achievements = state.recentAchievements,
            onDismiss = viewModel::dismissAchievementDialog
        )
    }
}

@Composable
private fun LoadingQuestionCard(
    shimmerEffect: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f + shimmerEffect * 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp,
                color = Color(0xFF8B6CB7)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Crafting your personalized question...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EnhancedQuestionCard(
    question: com.example.cailights.data.model.FoodForThoughtQuestion,
    hasAnsweredToday: Boolean,
    onAnswerClick: () -> Unit,
    onCardClick: () -> Unit,
    shimmerEffect: Float,
    modifier: Modifier = Modifier
) {
    val cardScale by animateFloatAsState(
        targetValue = if (hasAnsweredToday) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (hasAnsweredToday) {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.8f),
                                Color(0xFF66BB6A).copy(alpha = 0.7f)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF8B6CB7).copy(alpha = 0.9f + shimmerEffect * 0.1f),
                                Color(0xFFA58AC7).copy(alpha = 0.8f + shimmerEffect * 0.2f)
                            )
                        )
                    }
                )
                .padding(20.dp)
        ) {
            Column {
                // Category indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = when (question.category.name) {
                                "TECHNICAL" -> "⚡ Technical"
                                "CAREER" -> "🚀 Career"
                                "LEADERSHIP" -> "👑 Leadership"
                                else -> "💭 Reflection"
                            },
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    if (hasAnsweredToday) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = question.question,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
                
                // Personalization hint
                if (!question.personalizedFor.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 ${question.personalizedFor}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (hasAnsweredToday) Arrangement.Center else Arrangement.SpaceBetween
                ) {
                    if (hasAnsweredToday) {
                        Text(
                            text = "✨ Reflection completed",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Button(
                            onClick = onAnswerClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Start Reflecting")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "~${question.estimatedAnswerTime} min",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SimpleQuestionCard(
    onNavigateToFullScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateToFullScreen() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B6CB7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "What is the technical challenge you overcome this week?",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Tap to answer",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun EnhancedStatsRow(
    currentStreak: Int,
    thisMonthResponses: Int,
    hasNewAchievements: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        EnhancedStatCard(
            title = "Current Streak",
            value = "$currentStreak days",
            hasGlow = currentStreak >= 7,
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        EnhancedStatCard(
            title = "This Month",
            value = "$thisMonthResponses Logs",
            hasGlow = hasNewAchievements,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun EnhancedStatCard(
    title: String,
    value: String,
    hasGlow: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasGlow) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (hasGlow) 6.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (hasGlow) 
                    MaterialTheme.colorScheme.primary
                else 
                    MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EnhancedAnswerDialog(
    question: String,
    currentResponse: String,
    onResponseChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    isSubmitting: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "💭 Share Your Thoughts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = question,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = currentResponse,
                    onValueChange = onResponseChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Share your thoughts and insights...") },
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isSubmitting
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.weight(1f),
                        enabled = currentResponse.isNotBlank() && !isSubmitting
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementCelebrationDialog(
    achievements: List<com.example.cailights.data.model.ThoughtAchievement>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉",
                    fontSize = 32.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Achievement Unlocked!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                achievements.take(2).forEach { achievement ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = achievement.icon,
                                fontSize = 20.sp
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Column {
                                Text(
                                    text = achievement.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = achievement.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
