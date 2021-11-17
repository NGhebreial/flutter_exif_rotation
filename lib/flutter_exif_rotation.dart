import 'dart:async';

import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterExifRotation {
  static const MethodChannel _channel =
      const MethodChannel('flutter_exif_rotation');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Get the [path] of the image and fix the orientation.
  /// [internalFile] - on Android if a file stored inside the app's
  /// directory ( /data/data/{your package}/ ) you don't need the
  /// READ/WRITE_EXTERNAL_STORAGE permission to manipulate with it.
  /// So if you will not use files from external storage set this param to
  /// true. In this case you can remove  READ/WRITE_EXTERNAL_STORAGE
  /// permissions from the AndroidManifest
  /// Return the [File] with the exif data fixed
  static Future<File> rotateImage({
    required String path,
    bool internalFile = false,
  }) async =>
      await _rotateImageInternal(
        path: path,
        save: false,
        internalFile: internalFile,
      );

  /// Get the [path] of the image, fix the orientation and
  /// saves the file in the device.
  /// [internalFile] - on Android if a file stored inside the app's
  /// directory ( /data/data/{your package}/ ) you don't need the
  /// READ/WRITE_EXTERNAL_STORAGE permission to manipulate with it.
  /// So if you will not use files from external storage set this param to
  /// true. In this case you can remove  READ/WRITE_EXTERNAL_STORAGE
  /// permissions from the AndroidManifest
  /// Return the [File] with the exif data fixed
  static Future<File> rotateAndSaveImage({
    required String path,
    bool internalFile = false,
  }) async =>
      await _rotateImageInternal(
        path: path,
        save: true,
        internalFile: internalFile,
      );

  static Future<File> _rotateImageInternal({
    required String path,
    required bool save,
    required bool internalFile,
  }) async {
    assert(path != null);
    String filePath = await (_channel.invokeMethod(
      'rotateImage',
      <String, dynamic>{
        'path': path,
        'save': false,
        'internalFile': internalFile
      },
    ));

    return new File(filePath);
  }
}
