package com.savantarch.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.savantarch.design.CornerFamily
import com.savantarch.design.DsColors
import com.savantarch.design.DsImages
import com.savantarch.design.DsShapes
import com.savantarch.design.DsStrings
import com.savantarch.design.DsThemeMode
import com.savantarch.design.DsTypography
import com.savantarch.design.FontSpec
import com.savantarch.design.ShapeAppearance
import com.savantarch.design.isPlatformInDarkTheme
import com.savantarch.design.toColorScheme
import com.savantarch.design.toShapes
import com.savantarch.design.toTypography
import kotlinx.coroutines.flow.MutableStateFlow

// --- Theme ---

enum class AppThemeVariant {
    GLOBAL,
    EMERALD;

    fun resolveColors(isDark: Boolean): AppColors {
        return when (this) {
            GLOBAL -> if (isDark) GlobalDarkAppColors else GlobalLightAppColors
            EMERALD -> if (isDark) EmeraldDarkAppColors else EmeraldLightAppColors
        }
    }

    fun resolveShapes(): AppShapes {
        return when (this) {
            GLOBAL -> GlobalAppShapes
            EMERALD -> EmeraldAppShapes
        }
    }

    fun resolveTypography(): AppTypography {
        return when (this) {
            GLOBAL -> GlobalAppTypography
            EMERALD -> EmeraldAppTypography
        }
    }
}

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}

val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("No AppShapes provided")
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided")
}

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}

object AppThemeSettings {
    val currentVariantFlow = MutableStateFlow(AppThemeVariant.GLOBAL)
    var currentVariant: AppThemeVariant
        get() = currentVariantFlow.value
        set(value) {
            currentVariantFlow.value = value
        }

    val themeModeFlow = MutableStateFlow(DsThemeMode.SYSTEM)
    var themeMode: DsThemeMode
        get() = themeModeFlow.value
        set(value) {
            themeModeFlow.value = value
        }

    val isDark: Boolean
        get() = when (themeMode) {
            DsThemeMode.LIGHT -> false
            DsThemeMode.DARK -> true
            DsThemeMode.SYSTEM -> isPlatformInDarkTheme()
        }
}

@Composable
fun AppTheme(
    variant: AppThemeVariant = AppThemeSettings.currentVariant,
    isDark: Boolean = AppThemeSettings.isDark,
    content: @Composable () -> Unit
) {
    val (appColors, colorScheme) = remember(variant, isDark) {
        val resolved = variant.resolveColors(isDark)
        resolved to resolved.toColorScheme(isDark)
    }
    val (appShapes, shapes) = remember(variant) {
        val resolved = variant.resolveShapes()
        resolved to resolved.toShapes()
    }
    val (appTypography, typography) = remember(variant) {
        val resolved = variant.resolveTypography()
        resolved to resolved.toTypography()
    }

    CompositionLocalProvider(
        LocalAppColors provides appColors,
        LocalAppShapes provides appShapes,
        LocalAppTypography provides appTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            content = content
        )
    }
}

// --- Colors ---

private const val WHITE = 0xFFFFFFFFL
private const val BLACK = 0xFF000000L
private const val PURPLE_1 = 0xFF6200EEL
private const val PURPLE_DARK = 0xFFBB86FCL
private const val PURPLE_LIGHT_CONTAINER = 0xFFEADDFFL
private const val PURPLE_LIGHT_ON_CONTAINER = 0xFF21005DL
private const val PURPLE_PASTEL = 0xFFD0BCFFL
private const val PURPLE_DARK_CONTAINER = 0xFF4F378BL
private const val PURPLE_MID_DARK = 0xFF381E72L
private const val PURPLE_MID = 0xFF6750A4L
private const val TEAL_1 = 0xFF03DAC5L
private const val GREY_LIGHT = 0xFFFAFAFAL
private const val CHARCOAL = 0xFF121212L
private const val CHARCOAL_DARK = 0xFF1E1E1EL
private const val ORANGE_DARK = 0xFFE65100L
private const val ORANGE_LIGHT = 0xFFFFB74DL
private const val GREEN_LIGHT = 0xFF00875AL
private const val GREEN_DARK = 0xFF36B37EL
private const val GREY_PURPLE_LIGHT = 0xFFE8DEF8L
private const val GREY_PURPLE_DARK = 0xFF1D192BL
private const val GREY_PURPLE_DEEP = 0xFF332D41L
private const val GREY_PURPLE_MEDIUM = 0xFF4A4458L
private const val ROSE_DEEP = 0xFF7D5260L
private const val ROSE_LIGHT = 0xFFFFD8E4L
private const val ROSE_DARK = 0xFF31111DL
private const val ROSE_PASTEL = 0xFFEFB8C8L
private const val ROSE_MEDIUM = 0xFF492532L
private const val ROSE_DEEP_MEDIUM = 0xFF633B48L
private const val GREY_VERY_DARK = 0xFF1C1B1FL
private const val GREY_VERY_LIGHT = 0xFFE6E1E5L
private const val GREY_BLUE_LIGHT = 0xFFE7E0ECL
private const val GREY_BLUE_DARK = 0xFF49454FL
private const val GREY_BLUE_MEDIUM = 0xFFCAC4D0L
private const val GREY_CHARCOAL = 0xFF313033L
private const val GREY_WHITE = 0xFFF4EFF4L
private const val RED_DARK = 0xFFB3261EL
private const val RED_LIGHT = 0xFFF9DEDCL
private const val RED_VERY_DARK = 0xFF410E0BL
private const val RED_PASTEL = 0xFFF2B8B5L
private const val RED_DEEP = 0xFF601410L
private const val RED_MEDIUM = 0xFF8C1D18L
private const val GREY_MEDIUM = 0xFF79747EL
private const val GREY_MID = 0xFF938F99L
private const val GREEN_VERY_LIGHT = 0xFFD1F2E5L
private const val GREEN_VERY_DARK = 0xFF00291BL
private const val GREEN_DEEP = 0xFF005135L
private const val YELLOW = 0xFFFFEB3BL

internal object GlobalLightDsColors : DsColors {
    override val primary = PURPLE_1
    override val onPrimary = WHITE
    override val primaryContainer = PURPLE_LIGHT_CONTAINER
    override val onPrimaryContainer = PURPLE_LIGHT_ON_CONTAINER
    override val inversePrimary = PURPLE_PASTEL
    override val secondary = TEAL_1
    override val onSecondary = WHITE
    override val secondaryContainer = GREY_PURPLE_LIGHT
    override val onSecondaryContainer = GREY_PURPLE_DARK
    override val tertiary = ROSE_DEEP
    override val onTertiary = WHITE
    override val tertiaryContainer = ROSE_LIGHT
    override val onTertiaryContainer = ROSE_DARK
    override val background = GREY_LIGHT
    override val onBackground = GREY_VERY_DARK
    override val surface = WHITE
    override val onSurface = CHARCOAL
    override val surfaceVariant = GREY_BLUE_LIGHT
    override val onSurfaceVariant = GREY_BLUE_DARK
    override val surfaceTint = PURPLE_1
    override val inverseSurface = GREY_CHARCOAL
    override val inverseOnSurface = GREY_WHITE
    override val error = RED_DARK
    override val onError = WHITE
    override val errorContainer = RED_LIGHT
    override val onErrorContainer = RED_VERY_DARK
    override val outline = GREY_MEDIUM
    override val outlineVariant = GREY_BLUE_MEDIUM
    override val scrim = BLACK
}

internal object GlobalDarkDsColors : DsColors {
    override val primary = PURPLE_DARK
    override val onPrimary = PURPLE_MID_DARK
    override val primaryContainer = PURPLE_DARK_CONTAINER
    override val onPrimaryContainer = PURPLE_LIGHT_CONTAINER
    override val inversePrimary = PURPLE_MID
    override val secondary = TEAL_1
    override val onSecondary = GREY_PURPLE_DEEP
    override val secondaryContainer = GREY_PURPLE_MEDIUM
    override val onSecondaryContainer = GREY_PURPLE_LIGHT
    override val tertiary = ROSE_PASTEL
    override val onTertiary = ROSE_MEDIUM
    override val tertiaryContainer = ROSE_DEEP_MEDIUM
    override val onTertiaryContainer = ROSE_LIGHT
    override val background = CHARCOAL
    override val onBackground = GREY_VERY_LIGHT
    override val surface = CHARCOAL_DARK
    override val onSurface = WHITE
    override val surfaceVariant = GREY_BLUE_DARK
    override val onSurfaceVariant = GREY_BLUE_MEDIUM
    override val surfaceTint = PURPLE_DARK
    override val inverseSurface = GREY_VERY_LIGHT
    override val inverseOnSurface = GREY_CHARCOAL
    override val error = RED_PASTEL
    override val onError = RED_DEEP
    override val errorContainer = RED_MEDIUM
    override val onErrorContainer = RED_PASTEL
    override val outline = GREY_MID
    override val outlineVariant = GREY_BLUE_DARK
    override val scrim = BLACK
}

internal object EmeraldLightDsColors : DsColors {
    override val primary = GREEN_LIGHT
    override val onPrimary = WHITE
    override val primaryContainer = GREEN_VERY_LIGHT
    override val onPrimaryContainer = GREEN_VERY_DARK
    override val inversePrimary = GREEN_DARK
    override val secondary = TEAL_1
    override val onSecondary = WHITE
    override val secondaryContainer = GREY_PURPLE_LIGHT
    override val onSecondaryContainer = GREY_PURPLE_DARK
    override val tertiary = ROSE_DEEP
    override val onTertiary = WHITE
    override val tertiaryContainer = ROSE_LIGHT
    override val onTertiaryContainer = ROSE_DARK
    override val background = GREY_LIGHT
    override val onBackground = GREY_VERY_DARK
    override val surface = WHITE
    override val onSurface = CHARCOAL
    override val surfaceVariant = GREY_BLUE_LIGHT
    override val onSurfaceVariant = GREY_BLUE_DARK
    override val surfaceTint = GREEN_LIGHT
    override val inverseSurface = GREY_CHARCOAL
    override val inverseOnSurface = GREY_WHITE
    override val error = RED_DARK
    override val onError = WHITE
    override val errorContainer = RED_LIGHT
    override val onErrorContainer = RED_VERY_DARK
    override val outline = GREY_MEDIUM
    override val outlineVariant = GREY_BLUE_MEDIUM
    override val scrim = BLACK
}

internal object EmeraldDarkDsColors : DsColors {
    override val primary = GREEN_DARK
    override val onPrimary = PURPLE_MID_DARK
    override val primaryContainer = GREEN_DEEP
    override val onPrimaryContainer = GREEN_VERY_LIGHT
    override val inversePrimary = GREEN_LIGHT
    override val secondary = TEAL_1
    override val onSecondary = GREY_PURPLE_DEEP
    override val secondaryContainer = GREY_PURPLE_MEDIUM
    override val onSecondaryContainer = GREY_PURPLE_LIGHT
    override val tertiary = ROSE_PASTEL
    override val onTertiary = ROSE_MEDIUM
    override val tertiaryContainer = ROSE_DEEP_MEDIUM
    override val onTertiaryContainer = ROSE_LIGHT
    override val background = CHARCOAL
    override val onBackground = GREY_VERY_LIGHT
    override val surface = CHARCOAL_DARK
    override val onSurface = WHITE
    override val surfaceVariant = GREY_BLUE_DARK
    override val onSurfaceVariant = GREY_BLUE_MEDIUM
    override val surfaceTint = GREEN_DARK
    override val inverseSurface = GREY_VERY_LIGHT
    override val inverseOnSurface = GREY_CHARCOAL
    override val error = RED_PASTEL
    override val onError = RED_DEEP
    override val errorContainer = RED_MEDIUM
    override val onErrorContainer = RED_PASTEL
    override val outline = GREY_MID
    override val outlineVariant = GREY_BLUE_DARK
    override val scrim = BLACK
}

class AppColors(
    private val base: DsColors,
    val promoBrand: Long,
    val statusPending: Long,
    val warning: Long
) : DsColors by base

internal val GlobalLightAppColors = AppColors(
    base = GlobalLightDsColors,
    promoBrand = ORANGE_DARK,
    statusPending = YELLOW,
    warning = ORANGE_DARK
)

internal val GlobalDarkAppColors = AppColors(
    base = GlobalDarkDsColors,
    promoBrand = ORANGE_LIGHT,
    statusPending = YELLOW,
    warning = ORANGE_LIGHT
)

internal val EmeraldLightAppColors = AppColors(
    base = EmeraldLightDsColors,
    promoBrand = ORANGE_DARK,
    statusPending = YELLOW,
    warning = ORANGE_DARK
)

internal val EmeraldDarkAppColors = AppColors(
    base = EmeraldDarkDsColors,
    promoBrand = ORANGE_LIGHT,
    statusPending = YELLOW,
    warning = ORANGE_LIGHT
)

// --- Shapes ---

internal object GlobalDsShapes : DsShapes {
    override val extraSmall = ShapeAppearance(CornerFamily.ROUNDED, 4.0)
    override val small = ShapeAppearance(CornerFamily.ROUNDED, 8.0)
    override val medium = ShapeAppearance(CornerFamily.ROUNDED, 12.0)
    override val large = ShapeAppearance(CornerFamily.ROUNDED, 16.0)
    override val extraLarge = ShapeAppearance(CornerFamily.ROUNDED, 28.0)
}

internal object EmeraldDsShapes : DsShapes {
    override val extraSmall = ShapeAppearance(CornerFamily.CUT, 6.0)
    override val small = ShapeAppearance(CornerFamily.CUT, 10.0)
    override val medium = ShapeAppearance(CornerFamily.CUT, 14.0)
    override val large = ShapeAppearance(CornerFamily.CUT, 20.0)
    override val extraLarge = ShapeAppearance(CornerFamily.CUT, 32.0)
}

class AppShapes(
    private val base: DsShapes,
    val customCard: ShapeAppearance
) : DsShapes by base

internal val GlobalAppShapes = AppShapes(
    base = GlobalDsShapes,
    customCard = ShapeAppearance(CornerFamily.ROUNDED, 16.0)
)

internal val EmeraldAppShapes = AppShapes(
    base = EmeraldDsShapes,
    customCard = ShapeAppearance(CornerFamily.CUT, 24.0)
)

// --- Typography ---

internal object GlobalDsTypography : DsTypography {
    override val bodyLarge = FontSpec(16.0, 24.0)
    override val bodyMedium = FontSpec(14.0, 20.0)
    override val bodySmall = FontSpec(12.0, 16.0)
    override val displayLarge = FontSpec(57.0, 64.0)
    override val displayMedium = FontSpec(45.0, 52.0)
    override val displaySmall = FontSpec(36.0, 44.0)
    override val headlineLarge = FontSpec(32.0, 40.0)
    override val headlineMedium = FontSpec(28.0, 36.0)
    override val headlineSmall = FontSpec(24.0, 32.0)
    override val labelLarge = FontSpec(14.0, 20.0)
    override val labelMedium = FontSpec(12.0, 16.0)
    override val labelSmall = FontSpec(11.0, 16.0)
    override val titleLarge = FontSpec(22.0, 28.0)
    override val titleMedium = FontSpec(16.0, 24.0)
    override val titleSmall = FontSpec(14.0, 20.0)
}

internal object EmeraldDsTypography : DsTypography {
    override val bodyLarge = FontSpec(18.0, 26.0)
    override val bodyMedium = FontSpec(16.0, 22.0)
    override val bodySmall = FontSpec(14.0, 18.0)
    override val displayLarge = FontSpec(59.0, 66.0)
    override val displayMedium = FontSpec(47.0, 54.0)
    override val displaySmall = FontSpec(38.0, 46.0)
    override val headlineLarge = FontSpec(34.0, 42.0)
    override val headlineMedium = FontSpec(30.0, 38.0)
    override val headlineSmall = FontSpec(26.0, 34.0)
    override val labelLarge = FontSpec(16.0, 22.0)
    override val labelMedium = FontSpec(14.0, 18.0)
    override val labelSmall = FontSpec(13.0, 18.0)
    override val titleLarge = FontSpec(24.0, 30.0)
    override val titleMedium = FontSpec(18.0, 26.0)
    override val titleSmall = FontSpec(16.0, 22.0)
}

class AppTypography(
    private val base: DsTypography,
    val customTitle: FontSpec
) : DsTypography by base

internal val GlobalAppTypography = AppTypography(
    base = GlobalDsTypography,
    customTitle = FontSpec(24.0, 32.0)
)

internal val EmeraldAppTypography = AppTypography(
    base = EmeraldDsTypography,
    customTitle = FontSpec(28.0, 36.0)
)

// --- Strings ---

expect enum class AppStrings : DsStrings {
    APP_TITLE,
    WELCOME_MSG,
    DESCRIPTION_MSG,
    BTN_EXPLORE,
    WELCOME_USER
}

// --- Images ---

expect enum class AppImages : DsImages {
    LOGO
}
