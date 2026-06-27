package com.savantarch.shared

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

object CmpViewControllers {
    fun createShowcase(): UIViewController {
        return ComposeUIViewController {
            CmpShowcaseScreen()
        }
    }
}
