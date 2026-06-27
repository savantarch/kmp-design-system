import UIKit
import Shared

@main
final class AppDelegate: UIResponder {
    var window: UIWindow?
}

// MARK: - UIApplicationDelegate Conformance
extension AppDelegate: UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions:
            [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        let window = UIWindow(
            frame: UIScreen.main.bounds
        )

        window.rootViewController = MainViewController()
        window.makeKeyAndVisible()
        self.window = window
        return true
    }
}
