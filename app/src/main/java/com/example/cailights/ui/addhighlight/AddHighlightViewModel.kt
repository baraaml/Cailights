package com.example.cailights.ui.addhighlight

import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.w3c.dom.Text

class AddHighlightViewModel: ViewModel() {

    // the _underscore. It signals "This is the private, internal version of something."
    private val _state = MutableStateFlow(AddHighlightState())
    // private: means only ViewModel can touch it
    // Mutable: means "changeable.
    // stateFlow: holds the current version, and notify all watchers if version changed

    val state = _state.asStateFlow()
    // state now is a watcher
    // state: here serve as window that anyone can see the _state
    // latest version in real-time, but cannot reach through the glass and change it
    // provides a safe, public, read-only view of the data for the UI to observe.

    // Why Do We Do This? (The Big Picture)
    // This pattern enforces a strict, one-way flow of data,
    // which makes your app incredibly safe and predictable:
    // 1. A user taps a button on the View (the UI).
    // 2. The View tells the ViewModel (the Chef), "Hey, the user clicked 'Save'!"
    // 3. The ViewModel (the Chef) is the only one with permission to change the data.
    // 3.1. It modifies its private _state.
    // 4. Because _state is a Flow, it automatically sends the new version out.
    // 5. The public, read-only state gets the new version, and the View (which is watching state) automatically updates to show the new data.

    fun onAchievementTextChanged(newText: String) {
        _state.update {
            currentState ->
            currentState.copy(achievementText = newText)
        }
    }

    fun onTagTextChanged(newTag: String) {
        _state.update {
            currentState ->
            currentState.copy(selectedTag = newTag)
        }
    }

    fun onDateTextChanged(newDate: String) {
        _state.update { currentState ->
            currentState.copy(selectedDate = newDate)
        }
    }


    // This function is called when the user clicks the "Save" button
    fun saveHighlight() {
        // For now, we'll just print the state to the log to see it works.
        // Later, we'll call our backend here.
        println("Saving highlight: ${_state.value}")
        // TODO: Add logic to save to backend/database
    }
}