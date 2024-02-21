package com.nestor.uikit.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nestor.uikit.R
import com.nestor.uikit.theme.color.LocalSYColorScheme

private val fontFamilyKarla =
    FontFamily(Font(R.font.karla))

// Set of Material typography styles to start with
@Composable
fun getTypo() =
    Typography(
        bodyLarge = TextStyle(
            fontFamily = fontFamilyKarla,
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            lineHeight = 22.56.sp,
            letterSpacing = 0.sp,
            color = LocalSYColorScheme.current.secondary
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamilyKarla,
            fontWeight = FontWeight.W300,
            fontSize = 17.sp,
            lineHeight = 25.5.sp,
            letterSpacing = 0.sp,
            color = LocalSYColorScheme.current.secondary
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamilyKarla,
            fontWeight = FontWeight.W700,
            fontSize = 35.sp,
            lineHeight = 43.96.sp,
            letterSpacing = 0.sp,
            color = LocalSYColorScheme.current.primary
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamilyKarla,
            fontWeight = FontWeight.W700,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp,
            color = LocalSYColorScheme.current.secondary
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamilyKarla,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )