package com.example.cailights.ui.addhighlight

data class AddHighlightState(
    val achievementText: String = "",
    val selectedTag: String = "",
    val selectedDate: String = "July 27, 2025",
    val isSaving: Boolean = false
)
// date classes: tells compiler they're holding data
// the compiler understands this and automatically make some useful functions for them
// like .copy(), .equals(), .toString() and so on
// but for normal classes complier does not generate those functions