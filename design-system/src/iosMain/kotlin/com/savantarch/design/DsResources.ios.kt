package com.savantarch.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGFloat
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIFontWeightRegular
import platform.UIKit.UIFontWeightSemibold
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImageRenderingMode
import platform.UIKit.UIImageView
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIViewContentMode
import platform.UIKit.accessibilityLabel
import platform.UIKit.isAccessibilityElement
import platform.posix.memcpy

private const val BUNDLE_NAME = "design-system"

// --- Bundle & Platform Helpers ---

/**
 * Locates and returns the design system resource bundle (`design-system.bundle`) within this
 * bundle, falling back to the receiver bundle if not found.
 *
 * @return The resolved design system [NSBundle] instance.
 */
fun NSBundle.getDesignSystemBundle(): NSBundle {
    val bundlePath = pathForResource(name = BUNDLE_NAME, ofType = "bundle")
    return bundlePath?.let { NSBundle.bundleWithPath(it) } ?: this
}

actual fun isPlatformInDarkTheme(): Boolean {
    return UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}

// --- Colors ---

/**
 * Converts this [Double] value to a platform-native [CGFloat].
 *
 * @return The [CGFloat] representation of this double.
 */
fun Double.toCGFloat(): CGFloat = this

/**
 * Converts an ARGB hex [Long] (e.g. `0xFF6200EE`) to a native iOS [UIColor].
 *
 * @return The resolved native [UIColor] instance.
 */
fun Long.toUIColor(): UIColor {
    val a = ((this shr 24) and 0xFFL).toDouble() / 255.0
    val r = ((this shr 16) and 0xFFL).toDouble() / 255.0
    val g = ((this shr 8) and 0xFFL).toDouble() / 255.0
    val b = (this and 0xFFL).toDouble() / 255.0
    return UIColor(red = r, green = g, blue = b, alpha = a)
}

/**
 * Converts a Compose Multiplatform [androidx.compose.ui.graphics.Color] to a native iOS [UIColor].
 *
 * @return The resolved native [UIColor] instance.
 */
fun androidx.compose.ui.graphics.Color.toUiColor(): UIColor {
    return UIColor(
        red = this.red.toDouble(),
        green = this.green.toDouble(),
        blue = this.blue.toDouble(),
        alpha = this.alpha.toDouble()
    )
}

// --- Typography ---

private const val FONT_BOLD = "Outfit-Bold"
private const val FONT_REGULAR = "Outfit-Regular"

/**
 * Converts this platform-agnostic [FontSpec] into a native iOS [UIFont].
 *
 * Attempts to load the design system's custom font families ("Outfit-Regular" or "Outfit-Bold")
 * from the main bundle resources, falling back to the standard iOS system font if unavailable.
 *
 * @param isBold If true, attempts to load the bold typeface, otherwise loads the regular typeface.
 * @return The resolved native [UIFont] instance.
 */
fun FontSpec.toUIFont(isBold: Boolean = false): UIFont {
    val fontName = if (isBold) FONT_BOLD else FONT_REGULAR
    val weight = if (isBold) UIFontWeightSemibold else UIFontWeightRegular
    return UIFont.fontWithName(fontName, fontSize)
        ?: UIFont.systemFontOfSize(fontSize, weight)
}

val DsTypography.bodyLargeFont: UIFont get() = bodyLarge.toUIFont(isBold = false)
val DsTypography.bodyMediumFont: UIFont get() = bodyMedium.toUIFont(isBold = false)
val DsTypography.bodySmallFont: UIFont get() = bodySmall.toUIFont(isBold = false)
val DsTypography.displayLargeFont: UIFont get() = displayLarge.toUIFont(isBold = false)
val DsTypography.displayMediumFont: UIFont get() = displayMedium.toUIFont(isBold = false)
val DsTypography.displaySmallFont: UIFont get() = displaySmall.toUIFont(isBold = false)
val DsTypography.headlineLargeFont: UIFont get() = headlineLarge.toUIFont(isBold = false)
val DsTypography.headlineMediumFont: UIFont get() = headlineMedium.toUIFont(isBold = false)
val DsTypography.headlineSmallFont: UIFont get() = headlineSmall.toUIFont(isBold = false)
val DsTypography.labelLargeFont: UIFont get() = labelLarge.toUIFont(isBold = true)
val DsTypography.labelMediumFont: UIFont get() = labelMedium.toUIFont(isBold = true)
val DsTypography.labelSmallFont: UIFont get() = labelSmall.toUIFont(isBold = true)
val DsTypography.titleLargeFont: UIFont get() = titleLarge.toUIFont(isBold = false)
val DsTypography.titleMediumFont: UIFont get() = titleMedium.toUIFont(isBold = true)
val DsTypography.titleSmallFont: UIFont get() = titleSmall.toUIFont(isBold = true)

// --- Strings ---

/**
 * Extension function to format a Swift/Kotlin [String] using Cocoa's [NSString] engine.
 *
 * Supports formatting up to 4 arguments by mapping them to appropriate [NSString] initialization
 * hooks.
 *
 * @param args The format arguments to apply.
 * @return The formatted string.
 */
@OptIn(kotlinx.cinterop.BetaInteropApi::class)
fun String.formatWithArgs(vararg args: Any): String {
    if (args.isEmpty()) return this
    val nsString = when (args.size) {
        1 -> NSString.create(format = this, args = arrayOf(args[0]))
        2 -> NSString.create(format = this, args = arrayOf(args[0], args[1]))
        3 -> NSString.create(format = this, args = arrayOf(args[0], args[1], args[2]))
        4 -> NSString.create(format = this, args = arrayOf(args[0], args[1], args[2], args[3]))
        else -> NSString.create(format = this, args = arrayOf(args[0], args[1], args[2], args[3]))
    }
    return nsString.toString()
}

/**
 * Loads a localized string synchronously from the design system bundle using the specified key.
 *
 * @param key The translation key defined in the localized string tables.
 * @return The resolved localized string.
 */
fun stringResource(key: String): String {
    return NSBundle.mainBundle.getDesignSystemBundle()
        .localizedStringForKey(key, "", null)
}

/**
 * Loads a localized string synchronously from the design system bundle using the specified key
 * and formats it with the provided arguments.
 *
 * @param key The translation key defined in the localized string tables.
 * @param formatArgs The formatting arguments to apply.
 * @return The resolved and formatted localized string.
 */
fun stringResource(key: String, vararg formatArgs: Any): String {
    val raw = stringResource(key)
    return raw.formatWithArgs(*formatArgs)
}

/**
 * iOS-specific contract for design system string assets.
 * String keys generated or defined on iOS must implement this interface to resolve to bundle
 * localization keys.
 */
interface IosDsStrings : DsStrings {
    /**
     * Maps the string key to its corresponding iOS bundle localization key.
     *
     * @return The localization key [String].
     */
    fun toIosKey(): String
}

private fun DsStrings.toIosKeyName(): String {
    val iosStrings = this as? IosDsStrings
        ?: error("dsStrings must implement IosDsStrings on iOS")
    return iosStrings.toIosKey()
}

/**
 * Resolves and loads the localized string value for this [DsStrings] directly on iOS.
 */
val DsStrings.localized: String
    get() {
        val key = this.toIosKeyName()
        return NSBundle.mainBundle.getDesignSystemBundle()
            .localizedStringForKey(key, "", null)
    }

@Composable
actual fun stringResource(dsStrings: DsStrings): String {
    val key = dsStrings.toIosKeyName()
    return remember(dsStrings, key) {
        NSBundle.mainBundle.getDesignSystemBundle()
            .localizedStringForKey(key, "", null)
    }
}

@Composable
actual fun stringResource(dsStrings: DsStrings, vararg formatArgs: Any): String {
    val key = dsStrings.toIosKeyName()
    return remember(dsStrings, key, *formatArgs) {
        val raw = NSBundle.mainBundle.getDesignSystemBundle()
            .localizedStringForKey(key, "", null)
        raw.formatWithArgs(*formatArgs)
    }
}

// --- Images ---

/**
 * Resolves a native iOS [UIImage] by its name from this bundle, falling back to the main
 * application bundle.
 *
 * @param imageName The name of the image asset in the asset catalog.
 * @return The resolved [UIImage] instance, or null if it cannot be found.
 */
fun NSBundle.resolveImageFromBundle(imageName: String): UIImage? {
    return UIImage.imageNamed(
        name = imageName,
        inBundle = this,
        compatibleWithTraitCollection = null
    ) ?: UIImage.imageNamed(imageName)
}

/**
 * Loads a template image from this bundle by its name, configured with template rendering mode
 * to allow tinting filters.
 *
 * @param imageName The name of the image asset in the asset catalog.
 * @return The resolved template [UIImage].
 */
fun NSBundle.loadTemplateImage(imageName: String): UIImage {
    val rawImg = resolveImageFromBundle(imageName)
    return rawImg
        ?.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate)
        ?: UIImage()
}

/**
 * Loads an image from this bundle by its name, configured with template or original rendering modes
 * based on whether a tint color is specified.
 *
 * @param imageName The name of the image asset in the asset catalog.
 * @param tintColor Optional tint color. If not null, sets template rendering mode.
 * @return The resolved [UIImage].
 */
fun NSBundle.loadImage(imageName: String, tintColor: UIColor?): UIImage {
    val rawImg = resolveImageFromBundle(imageName) ?: UIImage()
    val mode = if (tintColor != null) {
        UIImageRenderingMode.UIImageRenderingModeAlwaysTemplate
    } else {
        UIImageRenderingMode.UIImageRenderingModeAlwaysOriginal
    }
    return rawImg.imageWithRenderingMode(mode)
}

/**
 * iOS-specific contract for design system image assets.
 * Image keys generated or defined on iOS must implement this interface to resolve to bundle
 * asset names.
 */
interface IosDsImages : DsImages {
    /**
     * Maps the image key to its corresponding iOS bundle asset name.
     *
     * @return The image asset name [String].
     */
    fun toImageName(): String
}

private fun DsImages.toIosImageName(): String {
    val iosImages = this as? IosDsImages
        ?: error("dsImages must implement IosDsImages on iOS")
    return iosImages.toImageName()
}

/**
 * Resolves and loads this [DsImages] key to a native iOS [UIImage] instance from our bundle.
 */
val DsImages.uiImage: UIImage
    get() {
        val name = this.toIosImageName()
        return NSBundle.mainBundle.getDesignSystemBundle()
            .loadTemplateImage(name)
    }

@Composable
actual fun DsImage(
    image: DsImages,
    contentDescription: String?,
    modifier: Modifier,
    tint: androidx.compose.ui.graphics.Color?,
    backgroundColor: androidx.compose.ui.graphics.Color?
) {
    val bundle = remember { NSBundle.mainBundle.getDesignSystemBundle() }
    val imageName = remember(image) { image.toIosImageName() }
    val tintColor = remember(tint) { tint?.toUiColor() }
    val bgColor =
        remember(backgroundColor) { backgroundColor?.toUiColor() ?: UIColor.clearColor }

    androidx.compose.ui.viewinterop.UIKitView(
        factory = {
            UIImageView().apply {
                this.image = bundle.loadImage(imageName, tintColor)
                if (tintColor != null) this.tintColor = tintColor
                this.backgroundColor = bgColor
                contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
                this.accessibilityLabel = contentDescription
                this.isAccessibilityElement = contentDescription != null
            }
        },
        update = { imageView ->
            imageView.image = bundle.loadImage(imageName, tintColor)
            if (tintColor != null) imageView.tintColor = tintColor
            imageView.backgroundColor = bgColor
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun DsImages.toCoilModel(): Any {
    return remember(this) {
        val iosImage = (this as? IosDsImages) ?: error("image must implement IosDsImages on iOS")
        val uiImage = iosImage.uiImage

        val data = UIImagePNGRepresentation(uiImage)
        if (data != null) {
            val bytes = ByteArray(data.length.toInt())
            bytes.usePinned { pinned ->
                memcpy(pinned.addressOf(0), data.bytes, data.length)
            }
            bytes
        } else {
            uiImage
        }
    }
}

