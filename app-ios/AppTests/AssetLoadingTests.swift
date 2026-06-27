import XCTest
@testable import app_ios
import Shared

final class AssetLoadingTests: XCTestCase {
    @MainActor func testLoadLogoImage() {
        // Log basic bundle locations
        let mainBundle = Bundle.main
        print("--- TEST DEBUG: Main Bundle path: \(mainBundle.bundlePath)")
        
        let bundlePath = mainBundle.path(forResource: "design-system", ofType: "bundle")
        print("--- TEST DEBUG: designSystem.bundle path: \(String(describing: bundlePath))")
        
        XCTAssertNotNil(bundlePath, "design-system.bundle should be present in the main bundle")
        
        if let path = bundlePath, let designSystemBundle = Bundle(path: path) {
            print("--- TEST DEBUG: Successfully loaded Bundle at path: \(path)")
            
            // Try loading image directly via UIImage in Swift
            let uiImage = UIImage(named: "ic_logo", in: designSystemBundle, compatibleWith: nil)
            print("--- TEST DEBUG: Direct UIImage load from designSystemBundle: \(String(describing: uiImage))")
            XCTAssertNotNil(uiImage, "Should load ic_logo directly from designSystemBundle")
            if let img = uiImage {
                print("--- TEST DEBUG: Image loaded. Size: \(img.size)")
            }
        }
        
        // Try loading via KMP AppImages.logo
        let logoImage = AppImages.logo.uiImage
        print("--- TEST DEBUG: AppImages.logo size: \(logoImage.size)")
        XCTAssertNotEqual(logoImage.size, .zero, "Logo image size should not be zero")
    }

    @MainActor func testLocalizedStrings() {
        let mainBundle = Bundle.main
        guard let bundlePath = mainBundle.path(forResource: "design-system", ofType: "bundle"),
              let designSystemBundle = Bundle(path: bundlePath) else {
            XCTFail("design-system.bundle should be present and loadable")
            return
        }

        // 1. Verify English strings load correctly
        guard let enLprojPath = designSystemBundle.path(forResource: "en", ofType: "lproj"),
              let enBundle = Bundle(path: enLprojPath) else {
            XCTFail("en.lproj sub-bundle should be present")
            return
        }
        let enTitle = enBundle.localizedString(forKey: "appTitle", value: nil, table: nil)
        XCTAssertEqual(enTitle, "KMP Design System", "English app title should load correctly")

        // 2. Verify Spanish strings load correctly (ensuring formatting/semicolons are valid)
        guard let esLprojPath = designSystemBundle.path(forResource: "es", ofType: "lproj"),
              let esBundle = Bundle(path: esLprojPath) else {
            XCTFail("es.lproj sub-bundle should be present")
            return
        }
        let esTitle = esBundle.localizedString(forKey: "appTitle", value: nil, table: nil)
        XCTAssertEqual(esTitle, "Sistema de Diseño KMP", "Spanish app title should load correctly")
    }
}
