package com.savantarch.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.savantarch.shared.AppThemeVariant
import com.savantarch.shared.AppThemeSettings
import com.savantarch.design.DsThemeMode
import com.google.android.material.bottomnavigation.*
import com.google.android.material.button.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val isEmerald = AppThemeSettings.currentVariant == AppThemeVariant.EMERALD
        val themeRes = if (isEmerald) {
            com.savantarch.shared.R.style.Theme_App_Emerald
        } else {
            com.savantarch.shared.R.style.Theme_App_Global
        }
        setTheme(themeRes)

        val mode = when (AppThemeSettings.themeMode) {
            DsThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DsThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            DsThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adjust status bar icon colors dynamically based on light/dark mode
        val isDark = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = !isDark

        // Setup bottom navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // Setup Theme Toggle
        val themeToggle = findViewById<MaterialButtonToggleGroup>(R.id.theme_toggle_group)
        val activeThemeId = if (isEmerald) {
            R.id.btn_theme_emerald
        } else {
            R.id.btn_theme_global
        }
        themeToggle.check(activeThemeId)

        themeToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val next = when (checkedId) {
                    R.id.btn_theme_emerald -> AppThemeVariant.EMERALD
                    else -> AppThemeVariant.GLOBAL
                }
                val active = AppThemeSettings.currentVariant
                if (next != active) {
                    AppThemeSettings.currentVariant = next
                    // recreate() is required because Android's stringResource and AppImage resolve 
                    // localized resources and drawables dynamically from the Android Context's XML theme attributes. 
                    // Recreating the Activity ensures a fresh Context is created with the new XML theme, 
                    // which in turn invalidates and updates Compose's remember(context) cache.
                    // 
                    // Note: If this app was Compose-only, we could avoid recreate() by wrapping the context in 
                    // ContextThemeWrapper inside AppTheme and providing it using `LocalContext provides themedContext`.
                    // But because this Activity uses hybrid/XML view components (like BottomNavigationView),
                    // recreate() is necessary to force the traditional Android View hierarchy to redraw and re-apply styles.
                    recreate()
                }
            }
        }

        // Setup Mode Toggle
        val modeToggle = findViewById<MaterialButtonToggleGroup>(R.id.mode_toggle_group)
        val activeModeId = if (AppThemeSettings.themeMode == DsThemeMode.DARK) {
            R.id.btn_mode_dark
        } else {
            R.id.btn_mode_light
        }
        modeToggle.check(activeModeId)

        modeToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val nextMode = if (checkedId == R.id.btn_mode_dark) DsThemeMode.DARK else DsThemeMode.LIGHT
                if (nextMode != AppThemeSettings.themeMode) {
                    AppThemeSettings.themeMode = nextMode
                    // Recreate the Activity to apply the new night/light configuration to the Context
                    // and invalidate Compose's remember(context) resource cache. (Same reason as above).
                    recreate()
                }
            }
        }
    }
}
