# Fix exif rotation for flutter

[![pub package](https://img.shields.io/pub/v/flutter_exif_rotation.svg)](https://pub.dartlang.org/packages/flutter_exif_rotation)

Flutter plugin that fixes the picture orientation for some devices.
In some devices the exif data shows picture in landscape mode when they're actually in portrait. 
This plugin fixes the orientation for pictures taken with those devices.

Every version of Android is supported and iOS is on the way.


## Installation

Add `flutter_exif_rotation` as a dependency in your `pubsec.yaml`

### Example

```dart
import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';

import 'package:image_picker/image_picker.dart';
import 'package:flutter_exif_rotation/flutter_exif_rotation.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File _image;

  Future getImage() async {
    File image = await ImagePicker.pickImage(source: ImageSource.gallery);
    image = await FlutterExifRotation.rotateImage(path: image.path);

    setState(() {
      _image = image;
    });
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Exif flutter rotation image example app'),
        ),
        body: new Center(
          child: _image == null
              ? new Text('No image selected.')
              : new Image.file(_image),
        ),
        floatingActionButton: new FloatingActionButton(
          onPressed: getImage,
          tooltip: 'Pick Image',
          child: new Icon(Icons.add),
        ),
      ),
    );
  }
}
```

