package com.example.cailights.ui.addhighlight

import java.text.SimpleDateFormat
import java.util.*

data class AddHighlightState(
    val achievementText: String = "",
    val selectedTag: String = "",
    val selectedDate: String = getCurrentDate(),
    val isSaving: Boolean = false,
    val showDatePicker: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    // Validation helpers
    val isFormValid: Boolean
        get() = achievementText.isNotBlank() && selectedTag.isNotBlank()
    
    val canSave: Boolean
        get() = isFormValid && !isSaving
}

private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

// Enhanced data class with modern features:
// - Form validation
// - Date picker state
// - Error handling
// - Success state
// - Helper properties for UI logic