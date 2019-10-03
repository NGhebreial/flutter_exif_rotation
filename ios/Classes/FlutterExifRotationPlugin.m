#import "FlutterExifRotationPlugin.h"
#import <flutter_exif_rotation/flutter_exif_rotation-Swift.h>

@implementation FlutterExifRotationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterExifRotationPlugin registerWithRegistrar:registrar];
}
@end
