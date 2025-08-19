package com.example.cailights.data.model

data class SavedHighlight(
    val id: String,
    val originalHighlightId: Int,
    val originalUserId: String,
    val categoryId: String,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)

data class SavedCategory(
    val id: String,
    val name: String,
    val description: String = "",
    val color: String = "#6200EE", // Default primary color
    val createdAt: Long = System.currentTimeMillis(),
    val highlightCount: Int = 0
)

data class HighlightWithSaveInfo(
    val highlight: com.example.cailights.ui.history.HighlightItem,
    val isSaved: Boolean = false,
    val savedCategories: List<SavedCategory> = emptyList()
)
