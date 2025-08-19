package com.example.cailights.ui.highlightdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.cailights.ui.history.HighlightItem
import com.example.cailights.data.model.SavedCategory
import com.example.cailights.data.model.SavedHighlight
import java.util.UUID

data class HighlightDetailState(
    val highlight: HighlightItem? = null,
    val isSaved: Boolean = false,
    val savedCategories: List<SavedCategory> = emptyList(),
    val availableCategories: List<SavedCategory> = emptyList(),
    val selectedCategories: Set<String> = emptySet(),
    val showSaveDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HighlightDetailViewModel : ViewModel() {
    private val _state = MutableStateFlow(HighlightDetailState())
    val state = _state.asStateFlow()

    init {
        loadAvailableCategories()
    }

    fun loadHighlight(highlightId: Int, userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                // TODO: Replace with actual repository call
                val highlight = getMockHighlight(highlightId, userId)
                val isSaved = checkIfHighlightIsSaved(highlightId)
                val savedCategories = if (isSaved) getSavedCategories(highlightId) else emptyList()

                _state.update {
                    it.copy(
                        highlight = highlight,
                        isSaved = isSaved,
                        savedCategories = savedCategories,
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

    fun toggleSave() {
        _state.update { currentState ->
            val newIsSaved = !currentState.isSaved
            currentState.copy(
                isSaved = newIsSaved,
                savedCategories = if (newIsSaved) {
                    // If saving for the first time, add to default "Saved" category
                    if (currentState.savedCategories.isEmpty()) {
                        listOf(getDefaultCategory())
                    } else {
                        currentState.savedCategories
                    }
                } else {
                    emptyList()
                },
                selectedCategories = if (newIsSaved && currentState.savedCategories.isEmpty()) {
                    setOf(getDefaultCategory().id)
                } else {
                    currentState.selectedCategories
                }
            )
        }
    }

    fun toggleCategory(category: SavedCategory) {
        _state.update { currentState ->
            val selectedCategories = currentState.selectedCategories.toMutableSet()
            if (selectedCategories.contains(category.id)) {
                selectedCategories.remove(category.id)
            } else {
                selectedCategories.add(category.id)
            }
            
            currentState.copy(
                selectedCategories = selectedCategories,
                savedCategories = currentState.availableCategories.filter { 
                    selectedCategories.contains(it.id) 
                }
            )
        }
    }

    fun createCategory(name: String, description: String) {
        val newCategory = SavedCategory(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            color = getRandomCategoryColor()
        )
        
        _state.update { currentState ->
            val updatedCategories = currentState.availableCategories + newCategory
            val updatedSelected = currentState.selectedCategories + newCategory.id
            
            currentState.copy(
                availableCategories = updatedCategories,
                selectedCategories = updatedSelected,
                savedCategories = updatedCategories.filter { updatedSelected.contains(it.id) }
            )
        }
    }

    fun showSaveDialog() {
        _state.update { 
            it.copy(
                showSaveDialog = true,
                selectedCategories = it.savedCategories.map { category -> category.id }.toSet()
            ) 
        }
    }

    fun dismissSaveDialog() {
        _state.update { it.copy(showSaveDialog = false) }
    }

    private fun loadAvailableCategories() {
        _state.update {
            it.copy(availableCategories = getMockCategories())
        }
    }

    // Mock data functions - replace with actual repository calls
    private fun getMockHighlight(highlightId: Int, userId: String): HighlightItem {
        // Find the actual highlight from the mock data based on the ID
        val allHighlights = createAllMockHighlights()
        return allHighlights.find { it.id == highlightId } ?: run {
            // Fallback if highlight not found
            HighlightItem(
                id = highlightId,
                date = "Today",
                title = "Highlight not found",
                fullContent = "This highlight could not be loaded.",
                tag = "achievement",
                username = "sample_user",
                userId = userId
            )
        }
    }

    private fun createAllMockHighlights(): List<HighlightItem> {
        val now = System.currentTimeMillis()
        val minuteInMillis = 60 * 1000L
        val hourInMillis = 60 * minuteInMillis
        val dayInMillis = 24 * hourInMillis

        return listOf(
            // From LatestViewModel - Feed highlights
            HighlightItem(101, "Today", "figured out how it's hard to keep wasting time in doing non important things.", "I realized that I've been spending too much time on activities that don't contribute to my goals. Need to be more intentional with my time.", "bootcamp", now - 5 * minuteInMillis, "gnanaraja", "user_101"),
            HighlightItem(102, "Today", "Made another career achievement", "Published technical blog post about Jetpack Compose, specially about state management which was featured in a popular newsletter.", "newAchievement", now - 61 * minuteInMillis, "amanda", "user_102"),
            HighlightItem(103, "Yesterday", "Completed advanced system design course", "Finished the distributed systems course and immediately applied the concepts to optimize our microservices architecture.", "lesson-learned", now - 4 * hourInMillis, "alex_dev", "user_103"),
            HighlightItem(104, "Yesterday", "Led my first team standup meeting", "Successfully facilitated the daily standup and helped the team identify blockers early. Felt confident in my leadership skills.", "newAchievement", now - 18 * hourInMillis, "sarah_codes", "user_104"),
            HighlightItem(105, "2 days ago", "Learned about React Native performance optimization", "Deep dive into React Native performance bottlenecks and how to use Flipper for debugging. Applied these techniques to reduce app startup time by 40%.", "bootcamp", now - 2 * dayInMillis, "dev_mike", "user_105"),
            HighlightItem(106, "3 days ago", "Fixed critical production bug under pressure", "Identified and resolved a memory leak that was causing crashes for 15% of users. The fix went out in a hotfix release within 2 hours.", "lesson-learned", now - 3 * dayInMillis, "bug_hunter", "user_106"),
            HighlightItem(107, "4 days ago", "Started contributing to open source project", "Made my first contribution to a popular UI library. The maintainers were very welcoming and provided great feedback on my PR.", "newAchievement", now - 4 * dayInMillis, "open_source_fan", "user_107"),
            
            // From HistoryViewModel - User's own highlights  
            HighlightItem(1, "26 July", "Joined the Frontend Bootcamp", "Started my journey in web development with the frontend bootcamp. Excited to learn React, JavaScript, and modern development practices.", "bootcamp", now - 5 * dayInMillis, "You", "current_user"),
            HighlightItem(2, "28 July", "Completed first React component", "Built my first functional component with hooks. Understanding the component lifecycle and state management better now.", "lesson-learned", now - 3 * dayInMillis, "You", "current_user"),
            HighlightItem(3, "30 July", "Learned CSS Grid and Flexbox", "Finally mastered CSS layout techniques. Created responsive layouts that work across different screen sizes.", "bootcamp", now - 1 * dayInMillis, "You", "current_user"),
            HighlightItem(4, "1 August", "Built my first full-stack app", "Created a todo app with React frontend and Node.js backend. Integrated with MongoDB and deployed to Heroku.", "newAchievement", now - 6 * hourInMillis, "You", "current_user"),
            HighlightItem(5, "Today", "Got my first job interview", "Applied to 20 companies and finally got a call back! Interview is scheduled for next week. Feeling nervous but excited.", "newAchievement", now - 2 * hourInMillis, "You", "current_user")
        )
    }

    private fun checkIfHighlightIsSaved(highlightId: Int): Boolean {
        // Mock implementation - check your saved highlights
        return false
    }

    private fun getSavedCategories(highlightId: Int): List<SavedCategory> {
        // Mock implementation - get categories this highlight is saved in
        return emptyList()
    }

    private fun getMockCategories(): List<SavedCategory> {
        return listOf(
            SavedCategory(
                id = "default",
                name = "Saved",
                description = "General saved highlights",
                color = "#6200EE"
            ),
            SavedCategory(
                id = "inspiration",
                name = "Inspiration",
                description = "Motivational and inspiring highlights",
                color = "#FF5722"
            ),
            SavedCategory(
                id = "learning",
                name = "Learning",
                description = "Educational content and lessons learned",
                color = "#4CAF50"
            ),
            SavedCategory(
                id = "career",
                name = "Career",
                description = "Professional development and achievements",
                color = "#2196F3"
            ),
            SavedCategory(
                id = "personal",
                name = "Personal",
                description = "Personal growth and development",
                color = "#9C27B0"
            )
        )
    }

    private fun getDefaultCategory(): SavedCategory {
        return SavedCategory(
            id = "default",
            name = "Saved",
            description = "General saved highlights",
            color = "#6200EE"
        )
    }

    private fun getRandomCategoryColor(): String {
        val colors = listOf(
            "#6200EE", "#FF5722", "#4CAF50", "#2196F3", "#9C27B0",
            "#FF9800", "#795548", "#607D8B", "#E91E63", "#009688"
        )
        return colors.random()
    }
}
