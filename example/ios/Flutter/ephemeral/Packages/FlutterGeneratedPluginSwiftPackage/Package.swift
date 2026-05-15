// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.
//
//  Generated file. Do not edit.
//

import PackageDescription

let package = Package(
    name: "FlutterGeneratedPluginSwiftPackage",
    platforms: [
        .iOS("13.0")
    ],
    products: [
        .library(name: "FlutterGeneratedPluginSwiftPackage", type: .static, targets: ["FlutterGeneratedPluginSwiftPackage"])
    ],
    dependencies: [
        .package(name: "image_picker_ios", path: "../.packages/image_picker_ios"),
        .package(name: "flutter_exif_rotation", path: "../.packages/flutter_exif_rotation")
    ],
    targets: [
        .target(
            name: "FlutterGeneratedPluginSwiftPackage",
            dependencies: [
                .product(name: "image-picker-ios", package: "image_picker_ios"),
                .product(name: "flutter-exif-rotation", package: "flutter_exif_rotation")
            ]
        )
    ]
)
