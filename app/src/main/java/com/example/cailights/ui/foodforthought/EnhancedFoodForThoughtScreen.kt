package com.example.cailights.ui.foodforthought

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cailights.data.model.*
import com.example.cailights.ui.foodforthought.*

@Composable
fun EnhancedFoodForThoughtScreen() {
    val viewModel: FoodForThoughtViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Animated background
    val infiniteTransition = rememberInfiniteTransition()
    val backgroundShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A0B2E).copy(alpha = 0.95f + backgroundShift * 0.05f),
                        Color(0xFF16213E).copy(alpha = 0.9f + backgroundShift * 0.1f),
                        Color(0xFF0F3460).copy(alpha = 0.85f + backgroundShift * 0.15f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated header
            AnimatedHeader()
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (state.isLoading) {
                LoadingContent()
            } else {
                state.todaysQuestion?.let { question ->
                    MainQuestionCard(
                        question = question,
                        hasAnsweredToday = state.hasAnsweredToday,
                        onAnswerClick = viewModel::showAnswerDialog,
                        backgroundShift = backgroundShift
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Community insights
                    if (state.communityInsights.isNotEmpty()) {
                        CommunityInsightsSection(insights = state.communityInsights)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
                    // Enhanced stats
                    state.currentStreak?.let { streak ->
                        EnhancedStatsGrid(streak = streak)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Achievement showcase
                    state.currentStreak?.achievements?.let { achievements ->
                        if (achievements.isNotEmpty()) {
                            AchievementShowcase(achievements = achievements)
                        }
                    }
                }
            }
        }
    }
    
    // Enhanced Answer Dialog
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
    
    // Achievement Dialog
    if (state.showAchievementDialog) {
        AchievementCelebrationDialog(
            achievements = state.recentAchievements,
            onDismiss = viewModel::dismissAchievementDialog
        )
    }
}

@Composable
private fun AnimatedHeader() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated brain icon with glow
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .rotate(rotation)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF9C27B0).copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 60f
                        ),
                        shape = CircleShape
                    )
            )
            
            Text(
                text = "🧠",
                fontSize = 48.sp,
                modifier = Modifier.scale(1.2f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Food for Thought",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Daily reflection for growth",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MainQuestionCard(
    question: FoodForThoughtQuestion,
    hasAnsweredToday: Boolean,
    onAnswerClick: () -> Unit,
    backgroundShift: Float
) {
    val cardScale by animateFloatAsState(
        targetValue = if (hasAnsweredToday) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable(enabled = !hasAnsweredToday) { onAnswerClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = if (hasAnsweredToday) {
                            listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.8f),
                                Color(0xFF45A049).copy(alpha = 0.9f)
                            )
                        } else {
                            listOf(
                                Color(0xFF6A1B9A).copy(alpha = 0.9f + backgroundShift * 0.1f),
                                Color(0xFF8E24AA).copy(alpha = 0.8f + backgroundShift * 0.2f),
                                Color(0xFFAB47BC).copy(alpha = 0.7f + backgroundShift * 0.3f)
                            )
                        }
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                // Question category and difficulty
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryChip(category = question.category)
                    DifficultyIndicator(difficulty = question.difficulty)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Main question
                Text(
                    text = question.question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    lineHeight = 28.sp
                )
                
                // Personalization note
                if (!question.personalizedFor.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 ${question.personalizedFor}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action button or completion status
                if (hasAnsweredToday) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reflection completed today",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Button(
                        onClick = onAnswerClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Think",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Reflecting",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "~${question.estimatedAnswerTime} min",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(category: ThoughtCategory) {
    val (emoji, color) = when (category) {
        ThoughtCategory.TECHNICAL -> "⚡" to Color(0xFF2196F3)
        ThoughtCategory.CAREER -> "🚀" to Color(0xFF4CAF50)
        ThoughtCategory.LEADERSHIP -> "👑" to Color(0xFFFF9800)
        ThoughtCategory.INNOVATION -> "💡" to Color(0xFFE91E63)
        ThoughtCategory.COLLABORATION -> "🤝" to Color(0xFF9C27B0)
        ThoughtCategory.LEARNING -> "📚" to Color(0xFF00BCD4)
        ThoughtCategory.WELLBEING -> "🌱" to Color(0xFF8BC34A)
        ThoughtCategory.REFLECTION -> "🎯" to Color(0xFF607D8B)
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.3f),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DifficultyIndicator(difficulty: QuestionDifficulty) {
    val stars = when (difficulty) {
        QuestionDifficulty.EASY -> 1
        QuestionDifficulty.MEDIUM -> 2
        QuestionDifficulty.HARD -> 3
    }
    
    Row {
        repeat(3) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Difficulty",
                tint = if (index < stars) Color(0xFFFFD700) else Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = Color(0xFF9C27B0),
            strokeWidth = 3.dp,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Crafting your personalized question...",
            color = Color.White.copy(alpha = alpha),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
