package com.example.cailights.ui.foodforthought

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.cailights.data.model.*

data class FoodForThoughtState(
    val todaysQuestion: FoodForThoughtQuestion? = null,
    val currentStreak: ThoughtStreak? = null,
    val hasAnsweredToday: Boolean = false,
    val isLoading: Boolean = false,
    val showAnswerDialog: Boolean = false,
    val currentResponse: String = "",
    val responseTime: Int = 0,
    val recentAchievements: List<ThoughtAchievement> = emptyList(),
    val showAchievementDialog: Boolean = false,
    val communityInsights: List<String> = emptyList(),
    val error: String? = null
)

class FoodForThoughtViewModel : ViewModel() {
    private val _state = MutableStateFlow(FoodForThoughtState())
    val state = _state.asStateFlow()

    init {
        loadTodaysQuestion()
        loadUserStreak()
    }

    fun showAnswerDialog() {
        _state.update { it.copy(showAnswerDialog = true, responseTime = 0) }
        startResponseTimer()
    }

    fun dismissAnswerDialog() {
        _state.update { 
            it.copy(
                showAnswerDialog = false, 
                currentResponse = "",
                responseTime = 0
            ) 
        }
    }

    fun updateResponse(response: String) {
        _state.update { it.copy(currentResponse = response) }
    }

    fun submitResponse() {
        viewModelScope.launch {
            val currentState = _state.value
            val question = currentState.todaysQuestion ?: return@launch
            
            _state.update { it.copy(isLoading = true) }
            
            try {
                // Create response
                val response = ThoughtResponse(
                    questionId = question.id,
                    userId = "current_user",
                    response = currentState.currentResponse,
                    responseTime = currentState.responseTime,
                    wordCount = currentState.currentResponse.split(" ").size
                )
                
                // TODO: Save to repository
                
                // Update streak
                val newStreak = updateStreak(currentState.currentStreak)
                val newAchievements = checkForAchievements(newStreak)
                
                _state.update {
                    it.copy(
                        hasAnsweredToday = true,
                        currentStreak = newStreak,
                        recentAchievements = newAchievements,
                        showAchievementDialog = newAchievements.isNotEmpty(),
                        showAnswerDialog = false,
                        currentResponse = "",
                        isLoading = false
                    )
                }
                
                // Generate next day's question based on this response
                generateNextQuestion(response)
                
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun dismissAchievementDialog() {
        _state.update { it.copy(showAchievementDialog = false) }
    }

    private fun loadTodaysQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                // TODO: Load from repository or generate based on user's recent activity
                val question = generatePersonalizedQuestion()
                val hasAnswered = checkIfAnsweredToday()
                val insights = getCommunityInsights()
                
                _state.update {
                    it.copy(
                        todaysQuestion = question,
                        hasAnsweredToday = hasAnswered,
                        communityInsights = insights,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun loadUserStreak() {
        viewModelScope.launch {
            // TODO: Load from repository
            val streak = getMockStreak()
            _state.update { it.copy(currentStreak = streak) }
        }
    }

    private fun startResponseTimer() {
        // TODO: Implement timer that counts response time
        // This helps understand user engagement and question difficulty
    }

    private fun updateStreak(currentStreak: ThoughtStreak?): ThoughtStreak {
        val current = currentStreak ?: getMockStreak()
        val today = java.time.LocalDate.now().toString()
        
        return if (current.lastResponseDate == today) {
            // Already answered today, don't update streak
            current
        } else {
            // New response, update streak
            current.copy(
                currentStreak = current.currentStreak + 1,
                longestStreak = maxOf(current.longestStreak, current.currentStreak + 1),
                totalResponses = current.totalResponses + 1,
                lastResponseDate = today,
                thisMonthResponses = current.thisMonthResponses + 1
            )
        }
    }

    private fun checkForAchievements(streak: ThoughtStreak): List<ThoughtAchievement> {
        val newAchievements = mutableListOf<ThoughtAchievement>()
        
        // Streak-based achievements
        when (streak.currentStreak) {
            7 -> newAchievements.add(createAchievement("WEEK_WARRIOR", "Week Warrior", "7 days of continuous reflection", "🔥", AchievementRarity.COMMON))
            30 -> newAchievements.add(createAchievement("MONTH_MASTER", "Month Master", "30 days of continuous reflection", "👑", AchievementRarity.RARE))
            100 -> newAchievements.add(createAchievement("CENTURY_THINKER", "Century Thinker", "100 days of continuous reflection", "💎", AchievementRarity.EPIC))
        }
        
        // Total response achievements
        when (streak.totalResponses) {
            10 -> newAchievements.add(createAchievement("THOUGHTFUL_STARTER", "Thoughtful Starter", "Completed 10 reflections", "🌱", AchievementRarity.COMMON))
            50 -> newAchievements.add(createAchievement("REFLECTION_EXPERT", "Reflection Expert", "Completed 50 reflections", "🧠", AchievementRarity.RARE))
            200 -> newAchievements.add(createAchievement("WISDOM_SEEKER", "Wisdom Seeker", "Completed 200 reflections", "🔮", AchievementRarity.LEGENDARY))
        }
        
        return newAchievements
    }

    private fun createAchievement(id: String, title: String, description: String, icon: String, rarity: AchievementRarity): ThoughtAchievement {
        return ThoughtAchievement(
            id = id,
            title = title,
            description = description,
            icon = icon,
            unlockedAt = System.currentTimeMillis(),
            rarity = rarity
        )
    }

    private fun generatePersonalizedQuestion(): FoodForThoughtQuestion {
        // TODO: Use AI/ML to generate questions based on:
        // 1. User's recent highlights and topics
        // 2. Career stage and interests
        // 3. Current trends in their field
        // 4. Previous question responses
        
        val personalizedQuestions = listOf(
            FoodForThoughtQuestion(
                id = "tech_challenge_${System.currentTimeMillis()}",
                question = "What's the most elegant solution you've implemented this week, and what made it beautiful?",
                category = ThoughtCategory.TECHNICAL,
                personalizedFor = "Based on your recent coding achievements",
                difficulty = QuestionDifficulty.MEDIUM,
                estimatedAnswerTime = 3,
                tags = listOf("problem-solving", "code-quality", "engineering"),
                communityStats = CommunityStats(
                    totalResponses = 1247,
                    averageResponseLength = 89,
                    topTags = listOf("algorithms", "architecture", "refactoring"),
                    responseDistribution = mapOf("short" to 30, "medium" to 50, "long" to 20)
                )
            ),
            FoodForThoughtQuestion(
                id = "growth_mindset_${System.currentTimeMillis()}",
                question = "If you could teach your past self one lesson about professional growth, what would it be?",
                category = ThoughtCategory.CAREER,
                personalizedFor = "Inspired by your career achievements",
                difficulty = QuestionDifficulty.HARD,
                estimatedAnswerTime = 4,
                tags = listOf("mentorship", "career-growth", "wisdom")
            ),
            FoodForThoughtQuestion(
                id = "team_dynamics_${System.currentTimeMillis()}",
                question = "Describe a moment when you turned a team conflict into a collaborative breakthrough.",
                category = ThoughtCategory.LEADERSHIP,
                personalizedFor = "Based on your leadership highlights",
                difficulty = QuestionDifficulty.HARD,
                estimatedAnswerTime = 5,
                tags = listOf("leadership", "conflict-resolution", "teamwork")
            )
        )
        
        return personalizedQuestions.random()
    }

    private fun getCommunityInsights(): List<String> {
        return listOf(
            "💡 Most developers mention 'debugging' as their biggest weekly challenge",
            "🚀 Career-focused questions get 2x more detailed responses",
            "🤝 Team collaboration insights are shared 3x more often",
            "⚡ Quick wins in automation are trending this week"
        )
    }

    private fun checkIfAnsweredToday(): Boolean {
        // TODO: Check repository
        return false
    }

    private fun generateNextQuestion(response: ThoughtResponse) {
        // TODO: Use response content to influence tomorrow's question
        // This creates a personalized journey of self-reflection
    }

    private fun getMockStreak(): ThoughtStreak {
        return ThoughtStreak(
            userId = "current_user",
            currentStreak = 12,
            longestStreak = 25,
            totalResponses = 89,
            lastResponseDate = java.time.LocalDate.now().minusDays(1).toString(),
            thisMonthResponses = 18,
            achievements = listOf(
                createAchievement("WEEK_WARRIOR", "Week Warrior", "7 days of continuous reflection", "🔥", AchievementRarity.COMMON),
                createAchievement("THOUGHTFUL_STARTER", "Thoughtful Starter", "Completed 10 reflections", "🌱", AchievementRarity.COMMON)
            )
        )
    }
}
