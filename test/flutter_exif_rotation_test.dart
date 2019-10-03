import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_exif_rotation/flutter_exif_rotation.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_exif_rotation');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterExifRotation.platformVersion, '42');
  });
}
