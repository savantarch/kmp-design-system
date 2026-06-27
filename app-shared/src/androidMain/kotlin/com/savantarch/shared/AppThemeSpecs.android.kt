package com.savantarch.shared

import com.savantarch.design.AndroidDsImages
import com.savantarch.design.AndroidDsStrings

// --- Strings ---

actual enum class AppStrings : AndroidDsStrings {
    APP_TITLE {
        override fun toAttrId(): Pair<Int, String> =
            Pair(R.attr.appTitle, AppThemeSettings.currentVariant.name)
    },
    WELCOME_MSG {
        override fun toAttrId(): Pair<Int, String> =
            Pair(R.attr.welcomeMsg, AppThemeSettings.currentVariant.name)
    },
    DESCRIPTION_MSG {
        override fun toAttrId(): Pair<Int, String> =
            Pair(R.attr.descriptionMsg, AppThemeSettings.currentVariant.name)
    },
    BTN_EXPLORE {
        override fun toAttrId(): Pair<Int, String> =
            Pair(R.attr.btnExplore, AppThemeSettings.currentVariant.name)
    },
    WELCOME_USER {
        override fun toAttrId(): Pair<Int, String> =
            Pair(R.attr.welcomeUser, AppThemeSettings.currentVariant.name)
    }
}

// --- Images ---

actual enum class AppImages : AndroidDsImages {
    LOGO;

    override fun toAttrId(): Int = R.attr.logo
}
