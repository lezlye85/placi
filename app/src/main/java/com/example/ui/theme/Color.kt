package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Modern Athletic Fitness Primary - Electric Indigo / Royal Blue
val PolishPrimary = Color(0xFF2563EB)
val PolishPrimaryDark = Color(0xFF1D4ED8)
val PolishOnPrimary = Color(0xFFFFFFFF)
val PolishPrimaryContainer = Color(0xFFDBEAFE)
val PolishOnPrimaryContainer = Color(0xFF1E3A8A)

// Secondary & Accent - Emerald Energy
val PolishSecondary = Color(0xFF059669)
val PolishOnSecondary = Color(0xFFFFFFFF)
val PolishSecondaryContainer = Color(0xFFD1FAE5)
val PolishOnSecondaryContainer = Color(0xFF064E3B)

// Neutral & Background Canvas (Light Mode)
val PolishBg = Color(0xFFF8FAFC)
val PolishSurface = Color(0xFFFFFFFF)
val PolishSurfaceVariant = Color(0xFFF1F5F9)
val PolishNavBg = Color(0xFFFFFFFF)
val PolishTextPrimary = Color(0xFF0F172A)
val PolishTextSecondary = Color(0xFF475569)
val PolishTextMuted = Color(0xFF94A3B8)
val PolishBorder = Color(0xFFE2E8F0)
val PolishBorderSubtle = Color(0xFFF1F5F9)
val PolishDarkNavy = Color(0xFF0F172A)

// Dedicated Macro Nutrition Tokens
val ProteinColor = Color(0xFF2563EB) // Royal Blue
val CarbsColor = Color(0xFFF59E0B)   // Golden Amber
val FatColor = Color(0xFFEF4444)     // Crimson Coral
val FiberColor = Color(0xFF10B981)   // Emerald Mint
val WaterColor = Color(0xFF06B6D4)   // Cyan Aqua
val BurnedCalColor = Color(0xFF8B5CF6) // Electric Violet
val PolishMacroBlue = ProteinColor

// Gradients for Hero Cards & Visual Polish
val HeroCalorieGradient = Brush.horizontalGradient(
    listOf(Color(0xFF2563EB), Color(0xFF06B6D4))
)
val HeroWorkoutGradient = Brush.horizontalGradient(
    listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
)
val HeroSupplementGradient = Brush.horizontalGradient(
    listOf(Color(0xFF059669), Color(0xFF10B981))
)
val CardShineGradient = Brush.verticalGradient(
    listOf(Color.White.copy(alpha = 0.8f), Color.White.copy(alpha = 0.4f))
)

// Dark Theme Variants - Midnight Titanium
val DarkPolishBg = Color(0xFF090D16)
val DarkPolishSurface = Color(0xFF131B2E)
val DarkPolishSurfaceVariant = Color(0xFF1E293B)
val DarkPolishNavBg = Color(0xFF0E1524)
val DarkPolishTextPrimary = Color(0xFFF8FAFC)
val DarkPolishTextSecondary = Color(0xFF94A3B8)
val DarkPolishBorder = Color(0xFF334155)
val DarkPolishPrimary = Color(0xFF60A5FA)
val DarkPolishPrimaryContainer = Color(0xFF1E3A8A)
val DarkPolishOnPrimaryContainer = Color(0xFFDBEAFE)

// Backwards-compatible aliases
val Emerald400 = PolishSecondary
val Emerald500 = PolishSecondary
val Emerald600 = Color(0xFF047857)
val LightBg = PolishBg
val DarkBg = DarkPolishBg
val LightSurface = PolishSurface
val DarkSurface = DarkPolishSurface
val LightSurfaceElevated = PolishSurfaceVariant
val DarkSurfaceElevated = DarkPolishSurfaceVariant
val LightBorder = PolishBorder
val DarkBorder = DarkPolishBorder
val LightTextPrimary = PolishTextPrimary
val DarkTextPrimary = DarkPolishTextPrimary
val LightTextSecondary = PolishTextSecondary
val DarkTextSecondary = DarkPolishTextSecondary




