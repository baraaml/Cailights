package com.example.cailights.data.model

import androidx.compose.ui.graphics.Color

data class FoodForThoughtQuestion(
    val id: String,
    val question: String,
    val category: ThoughtCategory,
    val personalizedFor: String? = null, // Based on user's recent highlights
    val difficulty: QuestionDifficulty = QuestionDifficulty.MEDIUM,
    val estimatedAnswerTime: Int = 2, // in minutes
    val tags: List<String> = emptyList(),
    val communityStats: CommunityStats? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class CommunityStats(
    val totalResponses: Int,
    val averageResponseLength: Int,
    val topTags: List<String>,
    val responseDistribution: Map<String, Int> // e.g., "short" -> 30, "medium" -> 50, "long" -> 20
)

enum class ThoughtCategory(
    val displayName: String,
    val color: Color,
    val icon: String
) {
    TECHNICAL("Technical Growth", Color(0xFF6366F1), "🔧"),
    CAREER("Career Development", Color(0xFF10B981), "🚀"),
    LEADERSHIP("Leadership", Color(0xFFF59E0B), "👑"),
    LEARNING("Learning & Skills", Color(0xFFEC4899), "🧠"),
    REFLECTION("Self Reflection", Color(0xFF8B5CF6), "🤔"),
    INNOVATION("Innovation & Ideas", Color(0xFF06B6D4), "💡"),
    COLLABORATION("Team & Collaboration", Color(0xFFEF4444), "🤝"),
    WELLBEING("Work-Life Balance", Color(0xFF84CC16), "🌱")
}

enum class QuestionDifficulty {
    EASY,    // Quick, surface-level questions
    MEDIUM,  // Thoughtful, requires some reflection
    HARD     // Deep, strategic thinking required
}

data class ThoughtResponse(
    val questionId: String,
    val userId: String,
    val response: String,
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = true,
    val responseTime: Int, // seconds taken to respond
    val wordCount: Int,
    val createdAt: Long = System.currentTimeMillis()
)

data class ThoughtStreak(
    val userId: String,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalResponses: Int,
    val lastResponseDate: String,
    val thisMonthResponses: Int,
    val achievements: List<ThoughtAchievement> = emptyList()
)

data class ThoughtAchievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val unlockedAt: Long,
    val rarity: AchievementRarity
)

enum class AchievementRarity(val color: Color) {
    COMMON(Color(0xFF6B7280)),
    RARE(Color(0xFF3B82F6)),
    EPIC(Color(0xFF8B5CF6)),
    LEGENDARY(Color(0xFFF59E0B))
}
