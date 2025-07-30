package com.example.cailights.ui.history.utils

import ProcessedHighlight
import com.example.cailights.ui.history.HighlightItem
import java.text.SimpleDateFormat
import java.util.*

object HighlightProcessor {

    /**
     * Processes highlights for timeline display, grouping by month
     * Same month highlights appear on the same side of the timeline
     */
    fun processHighlightsForTimeline(highlights: List<HighlightItem>): List<ProcessedHighlight> {
        if (highlights.isEmpty()) return emptyList()

        // Group highlights by month-year
        val groupedHighlights = groupHighlightsByMonth(highlights)

        // Process each group and assign sides alternately
        val processedHighlights = mutableListOf<ProcessedHighlight>()
        var isLeftSide = true // Start with left side

        groupedHighlights.forEach { (monthYear, monthHighlights) ->
            // All highlights in this month go to the same side
            monthHighlights.forEachIndexed { index, highlight ->
                processedHighlights.add(
                    ProcessedHighlight(
                        item = highlight,
                        originalIndex = highlights.indexOf(highlight),
                        isLeftAligned = isLeftSide
                    )
                )
            }
            // Switch sides for the next month
            isLeftSide = !isLeftSide
        }

        return processedHighlights
    }

    /**
     * Groups highlights by month-year, maintaining chronological order
     */
    private fun groupHighlightsByMonth(highlights: List<HighlightItem>): LinkedHashMap<String, List<HighlightItem>> {
        val dateFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        val monthYearFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)

        // Group highlights by month-year
        val grouped = LinkedHashMap<String, MutableList<HighlightItem>>()

        highlights.forEach { highlight ->
            try {
                val date = dateFormat.parse(highlight.date)
                date?.let {
                    // Set year to current year for parsing (since sample data doesn't include year)
                    val calendar = Calendar.getInstance()
                    calendar.time = it
                    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))

                    val monthYearKey = monthYearFormat.format(calendar.time)

                    if (!grouped.containsKey(monthYearKey)) {
                        grouped[monthYearKey] = mutableListOf()
                    }
                    grouped[monthYearKey]?.add(highlight)
                }
            } catch (e: Exception) {
                // If date parsing fails, create a fallback group
                val fallbackKey = "Unknown ${highlight.date}"
                if (!grouped.containsKey(fallbackKey)) {
                    grouped[fallbackKey] = mutableListOf()
                }
                grouped[fallbackKey]?.add(highlight)
            }
        }

        // Sort each month's highlights by date (most recent first within each month)
        grouped.forEach { (_, highlights) ->
            highlights.sortByDescending { highlight ->
                try {
                    dateFormat.parse(highlight.date)?.time ?: 0L
                } catch (e: Exception) {
                    0L
                }
            }
        }

        return grouped.mapValues { it.value.toList() } as LinkedHashMap<String, List<HighlightItem>>
    }

    /**
     * Search functionality for highlights
     */
    fun searchHighlights(highlights: List<HighlightItem>, query: String): List<HighlightItem> {
        if (query.isBlank()) return emptyList()

        val lowerQuery = query.lowercase()
        return highlights.filter { highlight ->
            highlight.title.lowercase().contains(lowerQuery) ||
                    highlight.fullContent.lowercase().contains(lowerQuery) ||
                    highlight.date.lowercase().contains(lowerQuery)
        }
    }
}