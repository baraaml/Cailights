package com.example.cailights.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.cailights.data.model.SavedCategory
import com.example.cailights.data.model.SavedHighlight
import com.example.cailights.ui.history.HighlightItem

data class SavedHighlightsState(
    val categories: List<SavedCategory> = emptyList(),
    val selectedCategory: SavedCategory? = null,
    val allSavedHighlights: List<HighlightItem> = emptyList(),
    val displayedHighlights: List<HighlightItem> = emptyList(),
    val savedHighlightsByCategory: Map<String, List<SavedHighlight>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SavedHighlightsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SavedHighlightsState())
    val state = _state.asStateFlow()

    init {
        loadSavedHighlights()
    }

    fun selectCategory(category: SavedCategory?) {
        _state.update { currentState ->
            val displayedHighlights = if (category == null) {
                currentState.allSavedHighlights
            } else {
                val savedInCategory = currentState.savedHighlightsByCategory[category.id] ?: emptyList()
                currentState.allSavedHighlights.filter { highlight ->
                    savedInCategory.any { it.originalHighlightId == highlight.id }
                }
            }
            
            currentState.copy(
                selectedCategory = category,
                displayedHighlights = displayedHighlights
            )
        }
    }

    fun removeFromCategory(highlightId: Int, userId: String) {
        viewModelScope.launch {
            // TODO: Implement actual removal from repository
            // For now, just update the UI
            _state.update { currentState ->
                val updatedSavedByCategory = currentState.savedHighlightsByCategory.toMutableMap()
                
                // Remove from selected category if one is selected
                if (currentState.selectedCategory != null) {
                    val categoryId = currentState.selectedCategory.id
                    val savedInCategory = updatedSavedByCategory[categoryId]?.toMutableList() ?: mutableListOf()
                    savedInCategory.removeAll { it.originalHighlightId == highlightId }
                    updatedSavedByCategory[categoryId] = savedInCategory
                } else {
                    // Remove from all categories
                    updatedSavedByCategory.keys.forEach { categoryId ->
                        val savedInCategory = updatedSavedByCategory[categoryId]?.toMutableList() ?: mutableListOf()
                        savedInCategory.removeAll { it.originalHighlightId == highlightId }
                        updatedSavedByCategory[categoryId] = savedInCategory
                    }
                }
                
                // Update displayed highlights
                val displayedHighlights = if (currentState.selectedCategory == null) {
                    currentState.allSavedHighlights.filter { it.id != highlightId }
                } else {
                    val savedInCategory = updatedSavedByCategory[currentState.selectedCategory.id] ?: emptyList()
                    currentState.allSavedHighlights.filter { highlight ->
                        savedInCategory.any { it.originalHighlightId == highlight.id }
                    }
                }
                
                currentState.copy(
                    savedHighlightsByCategory = updatedSavedByCategory,
                    displayedHighlights = displayedHighlights,
                    allSavedHighlights = currentState.allSavedHighlights.filter { it.id != highlightId }
                )
            }
        }
    }

    private fun loadSavedHighlights() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                // TODO: Replace with actual repository calls
                val categories = getMockCategories()
                val savedHighlights = getMockSavedHighlights()
                val savedByCategory = getMockSavedHighlightsByCategory()
                
                _state.update {
                    it.copy(
                        categories = categories,
                        allSavedHighlights = savedHighlights,
                        displayedHighlights = savedHighlights,
                        savedHighlightsByCategory = savedByCategory,
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

    // Mock data functions - replace with actual repository calls
    private fun getMockCategories(): List<SavedCategory> {
        return listOf(
            SavedCategory(
                id = "inspiration",
                name = "Inspiration",
                description = "Motivational and inspiring highlights",
                color = "#FF5722",
                highlightCount = 5
            ),
            SavedCategory(
                id = "learning",
                name = "Learning",
                description = "Educational content and lessons learned",
                color = "#4CAF50",
                highlightCount = 3
            ),
            SavedCategory(
                id = "career",
                name = "Career",
                description = "Professional development and achievements",
                color = "#2196F3",
                highlightCount = 4
            ),
            SavedCategory(
                id = "personal",
                name = "Personal",
                description = "Personal growth and development",
                color = "#9C27B0",
                highlightCount = 2
            )
        )
    }

    private fun getMockSavedHighlights(): List<HighlightItem> {
        return listOf(
            HighlightItem(
                id = 201,
                date = "14 Nov",
                title = "Open source contribution merged into popular library",
                fullContent = "My PR to add dark mode support was merged into a library with 10k+ stars. Excited to see it help the community!",
                tag = "open-source",
                username = "alex_codes",
                userId = "user_alex"
            ),
            HighlightItem(
                id = 301,
                date = "16 Nov",
                title = "Design system adoption reached 80% across teams",
                fullContent = "The design system I created is now used by 8 out of 10 product teams. This has reduced design inconsistencies by 60%.",
                tag = "design-systems",
                username = "priya_designs",
                userId = "user_priya"
            ),
            HighlightItem(
                id = 101,
                date = "15 Nov",
                title = "Launched new feature with 99.9% crash-free rate",
                fullContent = "Successfully shipped the new notification system with extensive testing and monitoring. Achieved 99.9% crash-free rate in the first week.",
                tag = "achievement",
                username = "sarah_dev",
                userId = "user_sarah"
            ),
            HighlightItem(
                id = 202,
                date = "10 Nov",
                title = "Reduced API response time by 40%",
                fullContent = "Optimized database queries and added caching layer. Response times went from 200ms to 120ms average.",
                tag = "performance",
                username = "alex_codes",
                userId = "user_alex"
            ),
            HighlightItem(
                id = 102,
                date = "12 Nov",
                title = "Mentored junior developer through first major project",
                fullContent = "Guided a junior team member through their first major feature implementation. They successfully delivered on time and learned valuable debugging skills.",
                tag = "mentoring",
                username = "sarah_dev",
                userId = "user_sarah"
            )
        )
    }

    private fun getMockSavedHighlightsByCategory(): Map<String, List<SavedHighlight>> {
        return mapOf(
            "inspiration" to listOf(
                SavedHighlight("1", 201, "user_alex", "inspiration"),
                SavedHighlight("2", 301, "user_priya", "inspiration"),
                SavedHighlight("3", 101, "user_sarah", "inspiration")
            ),
            "learning" to listOf(
                SavedHighlight("4", 202, "user_alex", "learning"),
                SavedHighlight("5", 102, "user_sarah", "learning")
            ),
            "career" to listOf(
                SavedHighlight("6", 101, "user_sarah", "career"),
                SavedHighlight("7", 201, "user_alex", "career"),
                SavedHighlight("8", 301, "user_priya", "career")
            ),
            "personal" to listOf(
                SavedHighlight("9", 102, "user_sarah", "personal"),
                SavedHighlight("10", 301, "user_priya", "personal")
            )
        )
    }
}
