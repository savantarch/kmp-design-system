import SwiftUI
import UIKit

extension Int64 {
    public var uiColor: UIColor {
        return self.toUIColor()
    }

    public var color: Color {
        return Color(self.uiColor)
    }
}
