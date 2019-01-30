import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterExifRotation {
  static const MethodChannel _channel =
      const MethodChannel('flutter_exif_rotation');

  /// Get the [path] of the image that is stored on the device
  /// return the [File] with the exif data fixed
  static Future<File> rotateImage({@required String path}) async {
    assert(path != null);

    String filePath = await _channel.invokeMethod(
      'rotateImage',
      <String, dynamic>{'path': path},
    );

    return new File(filePath);
  }
}
