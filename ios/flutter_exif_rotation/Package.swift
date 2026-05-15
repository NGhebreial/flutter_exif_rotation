// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    // TODO: Update your plugin name.
    name: "flutter_exif_rotation",
    platforms: [
        .iOS("13.0"),
    ],
    products: [
      .library(name: "flutter-exif-rotation", targets: ["flutter_exif_rotation"])
    ],
    dependencies: [
        .package(name: "FlutterFramework", path: "../FlutterFramework")
    ],
    targets: [
        .target(
            name: "flutter_exif_rotation",
            dependencies: [
                .product(name: "FlutterFramework", package: "FlutterFramework")
            ],
            path: "Sources/flutter_exif_rotation"
        )
    ]
)