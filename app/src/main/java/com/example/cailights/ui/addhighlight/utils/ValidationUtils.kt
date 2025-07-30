package com.example.cailights.ui.addhighlight.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private val displayDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    private val storageDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    fun getCurrentDisplayDate(): String {
        return displayDateFormat.format(Date())
    }
    
    fun getCurrentStorageDate(): String {
        return storageDateFormat.format(Date())
    }
    
    fun formatDateForDisplay(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return displayDateFormat.format(calendar.time)
    }
    
    fun formatDateForStorage(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return storageDateFormat.format(calendar.time)
    }
    
    fun isValidDate(dateString: String): Boolean {
        return try {
            displayDateFormat.parse(dateString)
            true
        } catch (e: Exception) {
            false
        }
    }
}

object ValidationUtils {
    
    fun isValidTag(tag: String): Boolean {
        return tag.isNotBlank() && tag.length >= 2 && tag.length <= 20
    }
    
    fun isValidAchievement(text: String): Boolean {
        return text.isNotBlank() && text.length >= 10
    }
    
    fun getTagError(tag: String): String? {
        return when {
            tag.isBlank() -> "Tag cannot be empty"
            tag.length < 2 -> "Tag must be at least 2 characters"
            tag.length > 20 -> "Tag must be less than 20 characters"
            else -> null
        }
    }
    
    fun getAchievementError(text: String): String? {
        return when {
            text.isBlank() -> "Please describe your achievement"
            text.length < 10 -> "Please provide more details (at least 10 characters)"
            else -> null
        }
    }
}
