package com.savantarch.design

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

// --- Context Attribute Helpers ---

/**
 * Resolves a dynamic theme-dependent string resource ID from the specified theme attribute.
 *
 * @param attr The theme attribute ID (e.g. `R.attr.someString`) to resolve.
 * @return The resolved [String] value, or an empty string if the attribute could not be resolved.
 */
fun Context.getStringFromAttr(attr: Int): String {
    val typedValue = TypedValue()
    if (theme.resolveAttribute(attr, typedValue, true)) {
        if (typedValue.resourceId != 0) {
            return getString(typedValue.resourceId)
        }
        return typedValue.string?.toString() ?: ""
    }
    return ""
}

/**
 * Resolves a dynamic theme-dependent string resource ID from the specified theme attribute
 * and formats it with the provided arguments.
 *
 * @param attr The theme attribute ID (e.g. `R.attr.someString`) to resolve.
 * @param formatArgs The formatting arguments to apply to the resolved string.
 * @return The formatted [String] value, or an empty string if the attribute could not be resolved.
 */
fun Context.getStringFromAttr(attr: Int, vararg formatArgs: Any): String {
    val raw = getStringFromAttr(attr)
    return String.format(raw, *formatArgs)
}

/**
 * Resolves a theme-dependent drawable resource ID from the specified theme attribute.
 *
 * @param attr The theme attribute ID (e.g. `R.attr.someDrawable`) to resolve.
 * @return The resolved drawable resource ID ([Int]), or 0 if the attribute could not be resolved.
 */
fun Context.getDrawableIdFromAttr(attr: Int): Int {
    val typedValue = TypedValue()
    if (theme.resolveAttribute(attr, typedValue, true)) {
        return typedValue.resourceId
    }
    return 0
}

actual fun isPlatformInDarkTheme(): Boolean {
    val uiMode = Resources.getSystem().configuration.uiMode
    return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

// --- Strings ---

/**
 * Android-specific contract for design system string assets.
 * String keys generated or defined on Android must implement this interface to resolve to theme
 * attributes.
 */
interface AndroidDsStrings : DsStrings {
    /**
     * Maps the string key to its corresponding Android theme attribute ID and optional variant
     * name.
     *
     * @return A [Pair] containing the theme attribute ID ([Int]) and the variant name ([String]).
     */
    fun toAttrId(): Pair<Int, String>
}

private fun DsStrings.toAndroidAttrId(): Pair<Int, String> {
    val androidStrings = this as? AndroidDsStrings
        ?: error("dsStrings must implement AndroidDsStrings on Android")
    return androidStrings.toAttrId()
}

@Composable
actual fun stringResource(dsStrings: DsStrings): String {
    val context = LocalContext.current
    val (attrId, variant) = dsStrings.toAndroidAttrId()
    return remember(dsStrings, attrId, variant) {
        context.getStringFromAttr(attrId)
    }
}

@Composable
actual fun stringResource(dsStrings: DsStrings, vararg formatArgs: Any): String {
    val context = LocalContext.current
    val (attrId, variant) = dsStrings.toAndroidAttrId()
    return remember(dsStrings, attrId, variant, *formatArgs) {
        context.getStringFromAttr(attrId, *formatArgs)
    }
}

// --- Images ---

/**
 * Android-specific contract for design system image assets.
 * Image keys generated or defined on Android must implement this interface to resolve to theme
 * attributes.
 */
interface AndroidDsImages : DsImages {
    /**
     * Maps the image key to its corresponding Android theme attribute ID.
     *
     * @return The theme attribute ID ([Int]) representing the image resource.
     */
    fun toAttrId(): Int
}

@Composable
actual fun DsImage(
    image: DsImages,
    contentDescription: String?,
    modifier: androidx.compose.ui.Modifier,
    tint: androidx.compose.ui.graphics.Color?,
    backgroundColor: androidx.compose.ui.graphics.Color?
) {
    val context = LocalContext.current
    val attrId = remember(image) {
        (image as? AndroidDsImages)?.toAttrId()
            ?: error("image must implement AndroidDsImages on Android")
    }
    val drawableResId = remember(attrId, context) {
        context.getDrawableIdFromAttr(attrId)
    }
    val colorFilter = remember(tint) {
        tint?.let { ColorFilter.tint(it) }
    }
    val finalModifier = remember(modifier, backgroundColor) {
        if (backgroundColor != null) {
            modifier.background(backgroundColor)
        } else {
            modifier
        }
    }
    androidx.compose.foundation.Image(
        painter = painterResource(drawableResId),
        contentDescription = contentDescription,
        modifier = finalModifier,
        colorFilter = colorFilter
    )
}

@Composable
actual fun DsImages.toCoilModel(): Any {
    val context = LocalContext.current
    val attrId = remember(this) {
        (this as? AndroidDsImages)?.toAttrId()
            ?: error("image must implement AndroidDsImages on Android")
    }
    return remember(attrId, context) {
        context.getDrawableIdFromAttr(attrId)
    }
}
