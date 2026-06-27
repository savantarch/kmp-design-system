import Shared
import SwiftUI
import UIKit

@MainActor
protocol ThemeUpdatable: AnyObject {
    func themeDidChange()
}

final class MainViewController: UIViewController {
    private let themeControl = UISegmentedControl(
        items: ["Global", "Emerald"]
    )
    private let modeControl = UISegmentedControl(
        items: ["Light", "Dark"]
    )
    private let containerView = UIView()
    private let tabBarVC = UITabBarController()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        applyTheme()
    }

    private func setupUI() {
        view.backgroundColor = AppThemeSwift.shared.colors.background.uiColor

        let headerStack = UIStackView()
        headerStack.axis = .vertical
        headerStack.spacing = 8
        headerStack.alignment = .center
        headerStack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(headerStack)

        themeControl.selectedSegmentIndex = AppThemeSwift.shared.currentVariant == .emerald ? 1 : 0
        themeControl.addTarget(
            self,
            action: #selector(themeChanged),
            for: .valueChanged
        )

        let isDark = traitCollection.userInterfaceStyle == .dark
        modeControl.selectedSegmentIndex = isDark ? 1 : 0
        modeControl.addTarget(
            self,
            action: #selector(modeChanged),
            for: .valueChanged
        )

        headerStack.addArrangedSubview(themeControl)
        headerStack.addArrangedSubview(modeControl)

        containerView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(containerView)

        NSLayoutConstraint.activate([
            headerStack.topAnchor.constraint(
                equalTo: view.safeAreaLayoutGuide.topAnchor,
                constant: 16
            ),
            headerStack.leadingAnchor.constraint(
                equalTo: view.leadingAnchor,
                constant: 16
            ),
            headerStack.trailingAnchor.constraint(
                equalTo: view.trailingAnchor,
                constant: -16
            ),

            themeControl.widthAnchor.constraint(
                equalTo: headerStack.widthAnchor,
                multiplier: 0.6
            ),
            modeControl.widthAnchor.constraint(
                equalTo: headerStack.widthAnchor,
                multiplier: 0.6
            ),

            containerView.topAnchor.constraint(
                equalTo: headerStack.bottomAnchor,
                constant: 16
            ),
            containerView.leadingAnchor.constraint(
                equalTo: view.leadingAnchor
            ),
            containerView.trailingAnchor.constraint(
                equalTo: view.trailingAnchor
            ),
            containerView.bottomAnchor.constraint(
                equalTo: view.bottomAnchor
            ),
        ])

        setupTabBar()
    }

    private func setupTabBar() {
        // Tab 1: UIKit
        let uikitVC = UIKitViewController()
        uikitVC.tabBarItem = UITabBarItem(
            title: "UIKit",
            image: UIImage(systemName: "paintpalette"),
            tag: 0
        )

        // Tab 2: SwiftUI
        let swiftuiVC = UIHostingController(
            rootView: SwiftUIContentView()
        )
        swiftuiVC.tabBarItem = UITabBarItem(
            title: "SwiftUI",
            image: UIImage(systemName: "swift"),
            tag: 1
        )

        // Tab 3: CMP
        let cmpVC = CmpHostViewController()
        cmpVC.tabBarItem = UITabBarItem(
            title: "CMP",
            image: UIImage(systemName: "cpu"),
            tag: 2
        )

        tabBarVC.viewControllers = [
            uikitVC,
            swiftuiVC,
            cmpVC,
        ]

        addChild(tabBarVC)
        containerView.addSubview(tabBarVC.view)
        tabBarVC.view.frame = containerView.bounds
        tabBarVC.view.autoresizingMask = [
            .flexibleWidth,
            .flexibleHeight,
        ]
        tabBarVC.didMove(toParent: self)
    }

    @objc private func themeChanged() {
        let isEmerald = themeControl.selectedSegmentIndex == 1
        let nextVariant: AppThemeVariant = isEmerald ? .emerald : .global

        AppThemeSettings.shared.currentVariant = nextVariant

        applyTheme()
        notifyChildren()
    }

    @objc private func modeChanged() {
        let isDark = modeControl.selectedSegmentIndex == 1

        overrideUserInterfaceStyle = isDark ? .dark : .light

        AppThemeSettings.shared.themeMode = isDark ? .dark : .light

        applyTheme()
        notifyChildren()
    }

    private func applyTheme() {
        view.backgroundColor = AppThemeSwift.shared.colors.background.uiColor
        tabBarVC.tabBar.tintColor = AppThemeSwift.shared.colors.primary.uiColor
        tabBarVC.tabBar.unselectedItemTintColor = .gray
        tabBarVC.tabBar.backgroundColor = AppThemeSwift.shared.colors.surface.uiColor
    }

    private func notifyChildren() {
        for vc in tabBarVC.viewControllers ?? [] {
            if let updatable = vc as? ThemeUpdatable {
                updatable.themeDidChange()
            }
        }
    }
}
