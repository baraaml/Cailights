package com.example.cailights.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cailights.ui.profile.ActivityDay

@Composable
fun ActivityGraphComponent(
    year: String,
    activityData: List<ActivityDay>
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = year,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Activity grid - 53 squares total
        // 3 rows of 15 squares each = 45 squares
        // 1 row of 8 squares = 8 squares
        // Total: 53 squares
        LazyVerticalGrid(
            columns = GridCells.Fixed(15),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.height(120.dp)
        ) {
            // Take only first 53 items from activityData, or pad with empty if needed
            val displayData = if (activityData.size >= 53) {
                activityData.take(53)
            } else {
                activityData + List(53 - activityData.size) { index -> 
                    ActivityDay(dayIndex = activityData.size + index, intensity = 0.0f) 
                }
            }
            
            items(displayData) { activityDay ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            MaterialTheme.colorScheme.secondary.copy(alpha = activityDay.intensity)
                        )
                )
            }
        }
    }
}
