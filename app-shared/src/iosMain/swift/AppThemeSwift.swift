import SwiftUI
import UIKit
import Combine
import Shared

@MainActor
public final class AppThemeSwift: ObservableObject {
    public static let shared = AppThemeSwift()

    private init() {
        Task { [weak self] in
            for await _ in AppThemeSettings.shared.currentVariantFlow {
                self?.objectWillChange.send()
            }
        }
        Task { [weak self] in
            for await _ in AppThemeSettings.shared.themeModeFlow {
                self?.objectWillChange.send()
            }
        }
    }

    public var currentVariant: AppThemeVariant {
        return AppThemeSettings.shared.currentVariant
    }

    public var themeMode: DsThemeMode {
        return AppThemeSettings.shared.themeMode
    }

    public var isDark: Bool {
        return AppThemeSettings.shared.isDark
    }

    public var colors: AppColors {
        return AppThemeSettings.shared.currentVariant.resolveColors(isDark: AppThemeSettings.shared.isDark)
    }

    public var shapes: AppShapes {
        return AppThemeSettings.shared.currentVariant.resolveShapes()
    }

    public var typography: AppTypography {
        return AppThemeSettings.shared.currentVariant.resolveTypography()
    }
}
