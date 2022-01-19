#import "FlutterExifRotationPlugin.h"
#if __has_include(<flutter_exif_rotation/flutter_exif_rotation-Swift.h>)
#import <flutter_exif_rotation/flutter_exif_rotation-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_exif_rotation-Swift.h"
#endif

@implementation FlutterExifRotationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterExifRotationPlugin registerWithRegistrar:registrar];
}
@end
