// swift-tools-version:6.0
import PackageDescription

let package = Package(
    name: "KMPDesignSystem",
    platforms: [
        .iOS(.v18)
    ],
    products: [
        .library(
            name: "KMPDesignSystem",
            targets: ["DesignSystem"]
        )
    ],
    dependencies: [],
    targets: [
        .binaryTarget(
            name: "DesignSystem",
            url: "https://github.com/savantarch/kmp-design-system/releases/download/v1.0.0-SNAPSHOT/DesignSystem.xcframework.zip",
            checksum: "PLACEHOLDER_CHECKSUM"
        )
    ]
)
