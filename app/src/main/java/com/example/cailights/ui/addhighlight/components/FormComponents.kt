package com.example.cailights.ui.addhighlight.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    minHeight: Int = 56,
    placeholder: String = ""
) {
    // Pre-computed styles for performance
    val textStyle = remember {
        TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF1C1C1E),
            fontWeight = FontWeight.Normal
        )
    }
    
    val labelStyle = remember {
        TextStyle(
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.Medium
        )
    }

    val fieldModifier = remember(minHeight) {
        Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Color(0xFFF8F9FA),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    }

    Column(modifier = modifier) {
        // Static label
        Text(
            text = label,
            style = labelStyle,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Optimized input field
        Row(
            modifier = fieldModifier,
            verticalAlignment = if (minHeight > 56) Alignment.Top else Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = if (isReadOnly) { {} } else onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .let { mod ->
                        if (minHeight > 56) mod.fillMaxHeight() else mod
                    },
                textStyle = textStyle,
                readOnly = isReadOnly,
                cursorBrush = SolidColor(Color(0xFF4A4AFF)),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = if (minHeight > 56) {
                            Modifier.fillMaxSize()
                        } else {
                            Modifier.fillMaxWidth()
                        }
                    ) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle.copy(color = Color(0xFF9E9E9E))
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            // Conditional trailing icon
            trailingIcon?.let { icon ->
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(enabled = onTrailingIconClick != null) {
                            onTrailingIconClick?.invoke()
                        }
                )
            }
        }
    }
}

@Composable
fun ModernDateField(
    value: String,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModernTextField(
        value = value,
        onValueChange = { },
        label = "Date",
        modifier = modifier.clickable { onDateClick() },
        isReadOnly = true,
        trailingIcon = Icons.Default.CalendarToday,
        onTrailingIconClick = onDateClick,
        placeholder = "Select date"
    )
}

@Composable
fun ModernTagField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ModernTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Tag",
        modifier = modifier,
        placeholder = "Enter a tag",
        trailingIcon = null
    )
}

@Composable
fun ModernAchievementField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ModernTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Achievement",
        modifier = modifier,
        minHeight = 120,
        placeholder = "What did you achieve today?"
    )
}
