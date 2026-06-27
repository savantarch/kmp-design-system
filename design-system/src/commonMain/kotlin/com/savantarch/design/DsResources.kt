package com.savantarch.design

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Theme Mode ---

/**
 * Defines the application's supported theme mode choices.
 */
enum class DsThemeMode {
    /** Forced Light Mode */
    LIGHT,

    /** Forced Dark Mode */
    DARK,

    /** Dynamic Mode matching system settings */
    SYSTEM
}

/**
 * Returns true if the host operating system platform is currently configured in dark theme.
 */
expect fun isPlatformInDarkTheme(): Boolean

// --- Colors ---

/**
 * Core design system color interface containing the standard set of Material 3 color properties.
 * Implementing enums or classes provide ARGB hex values as [Long]s (e.g. `0xFF6200EE`).
 *
 * @see [ColorScheme]
 */
interface DsColors {
    val primary: Long
    val onPrimary: Long
    val primaryContainer: Long
    val onPrimaryContainer: Long
    val inversePrimary: Long
    val secondary: Long
    val onSecondary: Long
    val secondaryContainer: Long
    val onSecondaryContainer: Long
    val tertiary: Long
    val onTertiary: Long
    val tertiaryContainer: Long
    val onTertiaryContainer: Long
    val background: Long
    val onBackground: Long
    val surface: Long
    val onSurface: Long
    val surfaceVariant: Long
    val onSurfaceVariant: Long
    val surfaceTint: Long
    val inverseSurface: Long
    val inverseOnSurface: Long
    val error: Long
    val onError: Long
    val errorContainer: Long
    val onErrorContainer: Long
    val outline: Long
    val outlineVariant: Long
    val scrim: Long
}

/**
 * Converts an ARGB hex [Long] (e.g. `0xFFFFFFFF`) directly to a Compose [Color].
 */
fun Long.toColor(): Color = Color(this)

/**
 * Maps this [DsColors] definition to a Jetpack Compose Material 3 [ColorScheme] instance.
 * 
 * @param isDark If true, maps to a [darkColorScheme], otherwise maps to a [lightColorScheme].
 */
fun DsColors.toColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = primary.toColor(),
            onPrimary = onPrimary.toColor(),
            primaryContainer = primaryContainer.toColor(),
            onPrimaryContainer = onPrimaryContainer.toColor(),
            inversePrimary = inversePrimary.toColor(),
            secondary = secondary.toColor(),
            onSecondary = onSecondary.toColor(),
            secondaryContainer = secondaryContainer.toColor(),
            onSecondaryContainer = onSecondaryContainer.toColor(),
            tertiary = tertiary.toColor(),
            onTertiary = onTertiary.toColor(),
            tertiaryContainer = tertiaryContainer.toColor(),
            onTertiaryContainer = onTertiaryContainer.toColor(),
            background = background.toColor(),
            onBackground = onBackground.toColor(),
            surface = surface.toColor(),
            onSurface = onSurface.toColor(),
            surfaceVariant = surfaceVariant.toColor(),
            onSurfaceVariant = onSurfaceVariant.toColor(),
            surfaceTint = surfaceTint.toColor(),
            inverseSurface = inverseSurface.toColor(),
            inverseOnSurface = inverseOnSurface.toColor(),
            error = error.toColor(),
            onError = onError.toColor(),
            errorContainer = errorContainer.toColor(),
            onErrorContainer = onErrorContainer.toColor(),
            outline = outline.toColor(),
            outlineVariant = outlineVariant.toColor(),
            scrim = scrim.toColor()
        )
    } else {
        lightColorScheme(
            primary = primary.toColor(),
            onPrimary = onPrimary.toColor(),
            primaryContainer = primaryContainer.toColor(),
            onPrimaryContainer = onPrimaryContainer.toColor(),
            inversePrimary = inversePrimary.toColor(),
            secondary = secondary.toColor(),
            onSecondary = onSecondary.toColor(),
            secondaryContainer = secondaryContainer.toColor(),
            onSecondaryContainer = onSecondaryContainer.toColor(),
            tertiary = tertiary.toColor(),
            onTertiary = onTertiary.toColor(),
            tertiaryContainer = tertiaryContainer.toColor(),
            onTertiaryContainer = onTertiaryContainer.toColor(),
            background = background.toColor(),
            onBackground = onBackground.toColor(),
            surface = surface.toColor(),
            onSurface = onSurface.toColor(),
            surfaceVariant = surfaceVariant.toColor(),
            onSurfaceVariant = onSurfaceVariant.toColor(),
            surfaceTint = surfaceTint.toColor(),
            inverseSurface = inverseSurface.toColor(),
            inverseOnSurface = inverseOnSurface.toColor(),
            error = error.toColor(),
            onError = onError.toColor(),
            errorContainer = errorContainer.toColor(),
            onErrorContainer = onErrorContainer.toColor(),
            outline = outline.toColor(),
            outlineVariant = outlineVariant.toColor(),
            scrim = scrim.toColor()
        )
    }
}

// --- Shapes ---

/**
 * Defines the style family of a corner.
 */
enum class CornerFamily {
    /** Standard rounded/curved corners */
    ROUNDED,

    /** Cut/beveled flat corners */
    CUT
}

/**
 * A platform-agnostic shape appearance specification declaring corner family and corner sizes.
 * These are compiled to standard Android shapes and Apple corner radii.
 *
 * @see [ShapeAppearance]
 */
data class ShapeAppearance(
    val family: CornerFamily,
    val topLeft: Double,
    val topRight: Double,
    val bottomRight: Double,
    val bottomLeft: Double
) {
    /**
     * Constructs a symmetric [ShapeAppearance] applying the same size to all four corners.
     *
     * @param family The style family of the corners.
     * @param size The corner size in dp/points to apply to all four corners.
     */
    constructor(family: CornerFamily, size: Double) : this(
        family = family,
        topLeft = size,
        topRight = size,
        bottomRight = size,
        bottomLeft = size
    )
}

/**
 * Core design system shapes interface containing the standard Material 3 corner scale.
 *
 * @see [Shapes]
 * @see [ShapeAppearance]
 */
interface DsShapes {
    val extraSmall: ShapeAppearance
    val small: ShapeAppearance
    val medium: ShapeAppearance
    val large: ShapeAppearance
    val extraLarge: ShapeAppearance
}

/**
 * Converts this [ShapeAppearance] into a Compose [CornerBasedShape].
 */
fun ShapeAppearance.toComposeShape(): CornerBasedShape {
    return when (family) {
        CornerFamily.ROUNDED -> RoundedCornerShape(
            topStart = topLeft.dp,
            topEnd = topRight.dp,
            bottomEnd = bottomRight.dp,
            bottomStart = bottomLeft.dp
        )

        CornerFamily.CUT -> CutCornerShape(
            topStart = topLeft.dp,
            topEnd = topRight.dp,
            bottomEnd = bottomRight.dp,
            bottomStart = bottomLeft.dp
        )
    }
}

/**
 * Maps this [DsShapes] definition to a Jetpack Compose Material 3 [Shapes] instance.
 */
fun DsShapes.toShapes(): Shapes {
    return Shapes(
        extraSmall = extraSmall.toComposeShape(),
        small = small.toComposeShape(),
        medium = medium.toComposeShape(),
        large = large.toComposeShape(),
        extraLarge = extraLarge.toComposeShape()
    )
}

// --- Typography ---

/**
 * A platform-agnostic text style/font token declaring target font size and line height.
 * These are compiled to standard Android text appearances and Apple system fonts.
 *
 * @property fontSize The text size in sp/points.
 * @property lineHeight The line height in sp/points.
 */
data class FontSpec(val fontSize: Double, val lineHeight: Double)

/**
 * Converts this [FontSpec] into a Compose [TextStyle].
 *
 * @param fontWeight The font weight (bold, medium, normal, etc.) to apply.
 */
fun FontSpec.toTextStyle(fontWeight: FontWeight = FontWeight.Normal): TextStyle = TextStyle(
    fontWeight = fontWeight,
    fontSize = fontSize.sp,
    lineHeight = lineHeight.sp
)

/**
 * Core design system typography interface containing the standard Material 3 text style tokens.
 *
 * @see [androidx.compose.material3.Typography]
 */
interface DsTypography {
    val bodyLarge: FontSpec
    val bodyMedium: FontSpec
    val bodySmall: FontSpec
    val displayLarge: FontSpec
    val displayMedium: FontSpec
    val displaySmall: FontSpec
    val headlineLarge: FontSpec
    val headlineMedium: FontSpec
    val headlineSmall: FontSpec
    val labelLarge: FontSpec
    val labelMedium: FontSpec
    val labelSmall: FontSpec
    val titleLarge: FontSpec
    val titleMedium: FontSpec
    val titleSmall: FontSpec
}

/**
 * Maps this [DsTypography] definition to a Jetpack Compose Material 3
 * [androidx.compose.material3.Typography] instance.
 */
fun DsTypography.toTypography(): androidx.compose.material3.Typography {
    return androidx.compose.material3.Typography(
        bodyLarge = bodyLarge.toTextStyle(FontWeight.Normal),
        bodyMedium = bodyMedium.toTextStyle(FontWeight.Normal),
        bodySmall = bodySmall.toTextStyle(FontWeight.Normal),
        displayLarge = displayLarge.toTextStyle(FontWeight.Normal),
        displayMedium = displayMedium.toTextStyle(FontWeight.Normal),
        displaySmall = displaySmall.toTextStyle(FontWeight.Normal),
        headlineLarge = headlineLarge.toTextStyle(FontWeight.Normal),
        headlineMedium = headlineMedium.toTextStyle(FontWeight.Normal),
        headlineSmall = headlineSmall.toTextStyle(FontWeight.Normal),
        labelLarge = labelLarge.toTextStyle(FontWeight.Medium),
        labelMedium = labelMedium.toTextStyle(FontWeight.Medium),
        labelSmall = labelSmall.toTextStyle(FontWeight.Medium),
        titleLarge = titleLarge.toTextStyle(FontWeight.Normal),
        titleMedium = titleMedium.toTextStyle(FontWeight.Medium),
        titleSmall = titleSmall.toTextStyle(FontWeight.Medium)
    )
}

// --- Strings ---

/**
 * Interface for application localized string keys.
 * Client enum keys should implement this interface.
 */
interface DsStrings

/**
 * Composable function to load a localized string synchronously.
 */
@Composable
expect fun stringResource(dsStrings: DsStrings): String

/**
 * Composable function to load a localized string synchronously and format it with arguments.
 */
@Composable
expect fun stringResource(dsStrings: DsStrings, vararg formatArgs: Any): String

// --- Images ---

/**
 * Interface for application graphics assets/images.
 * Client enum keys should implement this interface.
 */
interface DsImages

/**
 * Composable function to display a synchronous vector/raster image across Android and iOS.
 * 
 * @param image The target [DsImages] asset to display.
 * @param contentDescription Optional accessibility description.
 * @param modifier Compose [Modifier] to apply styling/layout.
 * @param tint Optional tint color filter to apply to the image.
 * @param backgroundColor Optional background color. On iOS, providing a solid background color
 * enables high-performance native rendering without memory copy and decoding overhead. If null,
 * a transparent Skia-based fallback is used.
 */
@Composable
expect fun DsImage(
    image: DsImages,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    backgroundColor: Color? = null
)

/**
 * Resolves the [DsImages] asset to a platform-specific model suitable for third-party image
 * loading libraries (such as Coil, Kamel, etc.) without introducing direct library dependencies
 * into the design system.
 *
 * ### Platform-Specific Behaviors:
 * - **Android**: Resolves dynamically from the active theme using the local context to return
 * the underlying raw drawable resource ID ([Int]). Since Coil natively supports resource IDs,
 * this is highly efficient, has virtually zero CPU/memory overhead, and integrates perfectly
 * with Android's native caching.
 * - **iOS**: Loads the asset from the dynamic design system bundle as a native `UIImage`, and
 * serializes it into a raw, lossless PNG [ByteArray] so that Coil 3's Skia rendering engine can
 * natively decode and display it.
 *
 * ### ⚠️ iOS Performance & Vector Trade-offs:
 * - **Redundant Pipeline:** Serializing a `UIImage` into PNG bytes (`UIImagePNGRepresentation`),
 * copying the memory across the cinterop bridge via `memcpy`, and then having Coil decode it
 * back into Skia pixels incurs significant CPU and memory overhead.
 * - **Rasterization:** Converting vector-based assets (e.g. PDF/SVG assets in the asset catalog)
 * to PNG bytes rasterizes them to a fixed pixel resolution, losing crisp vector scalability on
 * zoom/scale.
 * - **Recommendation:** Always prefer the native [DsImage] composable for local assets whenever
 * possible. It renders the image natively via a `UIImageView` using UIKit interop, bypassing
 * Skia entirely to achieve zero copy overhead and full crisp vector support.
 */
@Composable
expect fun DsImages.toCoilModel(): Any
