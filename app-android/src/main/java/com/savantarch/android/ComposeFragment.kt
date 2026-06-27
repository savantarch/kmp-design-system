package com.savantarch.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.savantarch.design.DsImage
import com.savantarch.design.stringResource
import com.savantarch.shared.*

class ComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeShowcaseScreen()
            }
        }
    }
}

@Composable
private fun ComposeShowcaseScreen(
    modifier: Modifier = Modifier
) {
    val currentVariant by AppThemeSettings.currentVariantFlow.collectAsState()
    AppThemeSettings.themeModeFlow.collectAsState() // Observe mode triggers

    AppTheme(
        variant = currentVariant,
        isDark = AppThemeSettings.isDark
    ) {
        val colors = MaterialTheme.colorScheme
        val shapes = MaterialTheme.shapes
        val typography = MaterialTheme.typography

        var isFavorite by rememberSaveable { mutableStateOf(false) }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main Product Card styled with medium shape token
            Card(
                shape = shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isFavorite) {
                        Surface(
                            color = colors.secondaryContainer,
                            shape = shapes.small,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "★ FAVORITED",
                                style = typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Header logo with tinted color token
                    DsImage(
                        image = AppImages.LOGO,
                        contentDescription = "Logo",
                        modifier = Modifier.size(64.dp),
                        tint = colors.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title: Product Name
                    Text(
                        text = "KMP Theming & Resource Engine",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle: Tagline
                    Text(
                        text = "SDK Tooling • Translation & Packaging Engine",
                        style = typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description text (using translation assets)
                    Text(
                        text = stringResource(AppStrings.DESCRIPTION_MSG),
                        style = typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formatted license assignment greeting
                    Text(
                        text = stringResource(AppStrings.WELCOME_USER, "Developer Workspace"),
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Interactive element: Premium state toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.background.copy(alpha = 0.5f), shape = shapes.small)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Mark as Favorite",
                            style = typography.labelMedium,
                            color = colors.onSurface
                        )
                        Switch(
                            checked = isFavorite,
                            onCheckedChange = { isFavorite = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.secondary,
                                checkedTrackColor = colors.secondaryContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action button styled with small shape token
                    Button(
                        onClick = { },
                        shape = shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(AppStrings.BTN_EXPLORE),
                            style = typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
