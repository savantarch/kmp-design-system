package com.savantarch.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.savantarch.design.DsImage
import com.savantarch.design.stringResource
import com.savantarch.design.toCoilModel
import com.savantarch.design.toComposeShape
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ShowcaseTab(val title: String) {
    COLORS("Colors"),
    TYPOGRAPHY("Typography"),
    SHAPES("Shapes"),
    ASSETS("Assets")
}

@Composable
fun CmpShowcaseScreen(
    modifier: Modifier = Modifier
) {
    val currentVariant by AppThemeSettings.currentVariantFlow.collectAsState()
    AppThemeSettings.themeModeFlow.collectAsState() // Observe mode triggers

    var activeTabOrdinal by rememberSaveable { mutableStateOf(ShowcaseTab.COLORS.ordinal) }
    val activeTab = ShowcaseTab.entries[activeTabOrdinal]

    AppTheme(
        variant = currentVariant,
        isDark = AppThemeSettings.isDark
    ) {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // --- Tab Selection Row ---
            TabRow(
                selectedTabIndex = activeTab.ordinal,
                containerColor = colors.surfaceVariant,
                contentColor = colors.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                ShowcaseTab.entries.forEach { tab ->
                    Tab(
                        selected = activeTab == tab,
                        onClick = { activeTabOrdinal = tab.ordinal },
                        text = {
                            Text(
                                text = tab.title,
                                style = typography.labelMedium,
                                fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // --- Content Panel ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (activeTab) {
                    ShowcaseTab.COLORS -> ColorsShowcase(colors)
                    ShowcaseTab.TYPOGRAPHY -> TypographyShowcase(colors)
                    ShowcaseTab.SHAPES -> ShapesShowcase(colors)
                    ShowcaseTab.ASSETS -> AssetsShowcase(colors)
                }
            }
        }
    }
}

@Composable
fun ColorsShowcase(colorScheme: androidx.compose.material3.ColorScheme) {
    val currentAppColors = AppTheme.colors

    val items = remember(colorScheme, currentAppColors) {
        listOf(
            "Primary" to (colorScheme.primary to currentAppColors.primary),
            "On Primary" to (colorScheme.onPrimary to currentAppColors.onPrimary),
            "Primary Container" to (colorScheme.primaryContainer to currentAppColors.primaryContainer),
            "On Primary Container" to (colorScheme.onPrimaryContainer to currentAppColors.onPrimaryContainer),
            "Secondary" to (colorScheme.secondary to currentAppColors.secondary),
            "On Secondary" to (colorScheme.onSecondary to currentAppColors.onSecondary),
            "Background" to (colorScheme.background to currentAppColors.background),
            "On Background" to (colorScheme.onBackground to currentAppColors.onBackground),
            "Surface" to (colorScheme.surface to currentAppColors.surface),
            "On Surface" to (colorScheme.onSurface to currentAppColors.onSurface),
            "Promo Brand (Custom)" to (Color(currentAppColors.promoBrand) to currentAppColors.promoBrand),
            "Status Pending (Custom)" to (Color(currentAppColors.statusPending) to currentAppColors.statusPending),
            "Warning (Custom)" to (Color(currentAppColors.warning) to currentAppColors.warning)
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { (name, colorPair) ->
            val (composeColor, rawLong) = colorPair
            val hexString = "0x" + rawLong.toString(16).uppercase()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(composeColor, shape = MaterialTheme.shapes.extraSmall)
                        .border(
                            1.dp,
                            colorScheme.outline.copy(alpha = 0.2f),
                            MaterialTheme.shapes.extraSmall
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = hexString,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TypographyShowcase(colorScheme: androidx.compose.material3.ColorScheme) {
    val customTypography = AppTheme.typography
    val materialTypography = MaterialTheme.typography

    val items = remember(customTypography, materialTypography) {
        listOf(
            "Display Large" to (materialTypography.displayLarge to customTypography.displayLarge),
            "Headline Medium" to (materialTypography.headlineMedium to customTypography.headlineMedium),
            "Title Large" to (materialTypography.titleLarge to customTypography.titleLarge),
            "Body Medium" to (materialTypography.bodyMedium to customTypography.bodyMedium),
            "Label Small" to (materialTypography.labelSmall to customTypography.labelSmall),
            "Custom" to (materialTypography.titleLarge.copy(
                fontSize = customTypography.customTitle.fontSize.sp,
                lineHeight = customTypography.customTitle.lineHeight.sp
            ) to customTypography.customTitle)
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { (name, stylePair) ->
            val (textStyle, fontSpec) = stylePair
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            ) {
                Text(
                    text = "$name (${fontSpec.fontSize} sp / ${fontSpec.lineHeight} sp)",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aa - Design System",
                    style = textStyle,
                    color = colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ShapesShowcase(colorScheme: androidx.compose.material3.ColorScheme) {
    val customShapes = AppTheme.shapes

    val items = remember(customShapes) {
        listOf(
            "Extra Small" to customShapes.extraSmall,
            "Small" to customShapes.small,
            "Medium" to customShapes.medium,
            "Large" to customShapes.large,
            "Extra Large" to customShapes.extraLarge,
            "Custom" to customShapes.customCard
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { (name, shapeSpec) ->
            val composeShape = shapeSpec.toComposeShape()
            val familyName = shapeSpec.family.name.lowercase().replaceFirstChar { it.uppercase() }
            val radiusInfo = "${shapeSpec.topLeft}dp"

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(colorScheme.primary, shape = composeShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Text(
                        text = "$familyName Corner Family ($radiusInfo radius)",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AssetsShowcase(colorScheme: androidx.compose.material3.ColorScheme) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Strings",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "WELCOME_MSG:",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(AppStrings.WELCOME_MSG),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "WELCOME_USER (Formatted):",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(AppStrings.WELCOME_USER, "Showcase User"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Graphics",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DsImage(
                            image = AppImages.LOGO,
                            contentDescription = "Original Logo",
                            modifier = Modifier.size(56.dp),
                            backgroundColor = colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "DsImage (Native)",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = AppImages.LOGO.toCoilModel(),
                            contentDescription = "Original Logo (Coil)",
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coil AsyncImage",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DsImage(
                            image = AppImages.LOGO,
                            contentDescription = "Tinted Logo",
                            modifier = Modifier.size(56.dp),
                            tint = colorScheme.primary,
                            backgroundColor = colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tinted (Primary)",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CmpShowcaseScreenPreview() {
    CmpShowcaseScreen()
}
