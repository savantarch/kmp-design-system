package com.savantarch.shared

import com.savantarch.design.DsImages
import com.savantarch.design.DsStrings
import com.savantarch.design.IosDsImages
import com.savantarch.design.IosDsStrings
import com.savantarch.design.localized
import com.savantarch.design.uiImage
import platform.UIKit.UIImage

// --- Strings ---

actual enum class AppStrings : IosDsStrings {
    APP_TITLE {
        override fun toIosKey(): String = "appTitle"
    },
    WELCOME_MSG {
        override fun toIosKey(): String =
            if (AppThemeSettings.currentVariant == AppThemeVariant.EMERALD) {
                "welcomeEmeraldMsg"
            } else {
                "welcomeMsg"
            }
    },
    DESCRIPTION_MSG {
        override fun toIosKey(): String = "descriptionMsg"
    },
    BTN_EXPLORE {
        override fun toIosKey(): String = "btnExplore"
    },
    WELCOME_USER {
        override fun toIosKey(): String = "welcomeUser"
    }
}

val AppStrings.localized: String
    get() = (this as DsStrings).localized

// --- Images ---

actual enum class AppImages : IosDsImages {
    LOGO;

    override fun toImageName(): String = "ic_logo"
}

val AppImages.uiImage: UIImage
    get() = (this as DsImages).uiImage
