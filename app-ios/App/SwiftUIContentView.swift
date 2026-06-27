import Shared
import SwiftUI

struct SwiftUIContentView: View {
    typealias ThemeVariant = AppThemeVariant

    @ObservedObject private var theme = AppThemeSwift.shared

    @State private var isFavorite = false

    private var descriptionMsg: String {
        AppStrings.descriptionMsg.localized
    }

    private var welcomeUserText: String {
        String(format: AppStrings.welcomeUser.localized, "Developer Workspace")
    }

    private var btnExploreText: String {
        AppStrings.btnExplore.localized
    }

    var body: some View {
        VStack {
            Spacer()

            // Main Product Card styled with medium shape token
            VStack(spacing: 0) {
                // Favorited Badge
                if isFavorite {
                    Text("★ FAVORITED")
                        .font(Font(theme.typography.labelSmallFont).bold())
                        .foregroundColor(theme.colors.onSecondaryContainer.color)
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(theme.colors.secondaryContainer.color)
                        .cornerRadius(CGFloat(theme.shapes.small.topLeft))
                        .padding(.bottom, 12)
                }

                // Header Logo
                Image(uiImage: AppImages.logo.uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 64, height: 64)
                    .foregroundColor(theme.colors.primary.color)

                Spacer().frame(height: 16)

                // Title: Product Name
                Text("KMP Theming & Resource Engine")
                    .font(Font(theme.typography.titleLargeFont).bold())
                    .foregroundColor(theme.colors.onSurface.color)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 4)

                // Subtitle: Tagline
                Text("SDK Tooling • Translation & Packaging Engine")
                    .font(Font(theme.typography.labelMediumFont).weight(.semibold))
                    .foregroundColor(theme.colors.primary.color)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 12)

                // Description Text
                Text(descriptionMsg)
                    .font(Font(theme.typography.bodyMediumFont))
                    .foregroundColor(theme.colors.onSurfaceVariant.color)
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 12)

                // License Assignment Text
                Text(welcomeUserText)
                    .font(Font(theme.typography.bodySmallFont))
                    .foregroundColor(theme.colors.onSurfaceVariant.color.opacity(0.8))
                    .multilineTextAlignment(.center)

                Spacer().frame(height: 24)

                // Interactive Switch element
                HStack {
                    Text("Mark as Favorite")
                        .font(Font(theme.typography.labelMediumFont))
                        .foregroundColor(theme.colors.onSurface.color)
                    Spacer()
                    Toggle("", isOn: $isFavorite)
                        .labelsHidden()
                        .toggleStyle(SwitchToggleStyle(tint: theme.colors.secondary.color))
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(theme.colors.background.color.opacity(0.5))
                .cornerRadius(CGFloat(theme.shapes.small.topLeft))

                Spacer().frame(height: 20)

                // Action Button styled with small shape token
                Button(action: {}) {
                    Text(btnExploreText)
                        .font(Font(theme.typography.labelMediumFont).bold())
                        .foregroundColor(theme.colors.onPrimary.color)
                        .frame(maxWidth: .infinity)
                        .frame(height: 48)
                        .background(theme.colors.primary.color)
                        .cornerRadius(CGFloat(theme.shapes.small.topLeft))
                }
            }
            .padding(24)
            .background(theme.colors.surfaceVariant.color.opacity(0.6))
            .cornerRadius(CGFloat(theme.shapes.medium.topLeft))
            .shadow(color: Color.black.opacity(0.05), radius: 8, x: 0, y: 4)

            Spacer()
        }
        .padding(.horizontal, 24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.colors.background.color)
        .ignoresSafeArea()
    }
}

#Preview {
    SwiftUIContentView()
}
