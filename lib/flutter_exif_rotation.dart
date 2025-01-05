import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class FlutterExifRotation {
  static const MethodChannel _channel =
      const MethodChannel('flutter_exif_rotation');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Get the [path] of the image and fix the orientation.
  /// The original file is overwritten or saved to
  /// [outputPath] if provided.
  /// Return the [File] with the exif data fixed.
  static Future<File> rotateImage({
    required String path,
    String? outputPath,
  }) async =>
      await _rotateImageInternal(
        path: path,
        outputPath: outputPath,
        save: false,
      );

  /// Get the [path] of the image, fix the orientation and
  /// saves the file in the device.
  /// The original file is overwritten or saved to
  /// [outputPath] if provided.
  /// Return the [File] with the exif data fixed
  static Future<File> rotateAndSaveImage({
    required String path,
    String? outputPath,
  }) async =>
      await _rotateImageInternal(
        path: path,
        outputPath: outputPath,
        save: true,
      );

  static Future<File> _rotateImageInternal({
    required String path,
    required String? outputPath,
    required bool save,
  }) async {
    String filePath = await (_channel.invokeMethod(
      'rotateImage',
      <String, dynamic>{
        'path': path,
        'outputPath': outputPath,
        'save': false,
      },
    ));

    return File(outputPath ?? filePath);
  }
}
