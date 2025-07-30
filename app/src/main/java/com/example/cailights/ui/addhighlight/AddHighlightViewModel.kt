package com.example.cailights.ui.addhighlight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddHighlightViewModel: ViewModel() {

    private val _state = MutableStateFlow(AddHighlightState())
    val state = _state.asStateFlow()

    fun onAchievementTextChanged(newText: String) {
        _state.update { currentState ->
            currentState.copy(
                achievementText = newText,
                errorMessage = null // Clear error when user types
            )
        }
    }

    fun onTagTextChanged(newTag: String) {
        _state.update { currentState ->
            currentState.copy(
                selectedTag = newTag,
                errorMessage = null
            )
        }
    }

    fun onDateTextChanged(newDate: String) {
        _state.update { currentState ->
            currentState.copy(selectedDate = newDate)
        }
    }

    fun onDatePickerToggled() {
        _state.update { currentState ->
            currentState.copy(showDatePicker = !currentState.showDatePicker)
        }
    }

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        
        _state.update { currentState ->
            currentState.copy(
                selectedDate = formattedDate,
                showDatePicker = false
            )
        }
    }

    fun clearError() {
        _state.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }

    fun clearSuccess() {
        _state.update { currentState ->
            currentState.copy(isSuccess = false)
        }
    }

    fun saveHighlight() {
        val currentState = _state.value
        
        // Fast validation
        if (!currentState.isFormValid) {
            _state.update { 
                it.copy(errorMessage = "Please fill in all required fields") 
            }
            return
        }

        // Instant loading state
        _state.update { 
            it.copy(
                isSaving = true, 
                errorMessage = null
            ) 
        }

        // Fast save operation
        viewModelScope.launch {
            try {
                // Minimal delay for fast UX (just for visual feedback)
                delay(300) // Reduced from 1500ms to 300ms
                
                // Log the data
                println("Saving highlight: ${currentState}")
                
                // Instant success state
                _state.update { 
                    it.copy(
                        isSaving = false,
                        isSuccess = true
                    ) 
                }
                
                // Fast form reset
                delay(100) // Minimal delay
                resetForm()
                
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to save highlight. Please try again."
                    ) 
                }
            }
        }
    }

    private fun resetForm() {
        _state.update { 
            AddHighlightState() // Reset to initial state
        }
    }
}