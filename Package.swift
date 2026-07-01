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
            url: "https://github.com/savantarch/kmp-design-system/releases/download/v1.0.0-beta08/DesignSystem.xcframework.zip",
            checksum: "cc71a704c854952e90a72f1af286e25b97ee640db60c15761d68c275a2c2ea6c"
        )
    ]
)
