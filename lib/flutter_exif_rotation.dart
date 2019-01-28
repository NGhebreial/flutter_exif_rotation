import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterExifRotation {
  static const MethodChannel _channel =
      const MethodChannel('flutter_exif_rotation');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<File> rotateImage({@required String path}) async {
    assert(path != null);

    String filePath = await _channel.invokeMethod(
      'rotateImage',
      <String, dynamic>{
        'path': path
      },
    );
    debugPrint("Rotated filePath:" + filePath);

    return new File(filePath);
  }

}
