import Shared
import UIKit

final class CmpHostViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        setupCMP()
    }

    private func setupCMP() {
        view.backgroundColor = AppThemeSwift.shared.colors.background.uiColor

        // Instantiate Compose Multiplatform
        // UIViewController exported from Kotlin
        let cmpVC = CmpViewControllers.shared.createShowcase()

        addChild(cmpVC)
        cmpVC.view.frame = view.bounds
        cmpVC.view.autoresizingMask = [
            .flexibleWidth,
            .flexibleHeight,
        ]
        view.addSubview(cmpVC.view)
        cmpVC.didMove(toParent: self)
    }
}
