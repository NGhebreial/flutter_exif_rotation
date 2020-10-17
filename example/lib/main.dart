import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_exif_rotation/flutter_exif_rotation.dart';
import 'package:image_picker/image_picker.dart';

void main() => runApp(MyApp());

/// The stateful widget
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

/// The class with the scaffold
class _MyAppState extends State<MyApp> {
  File _image;
  final picker = ImagePicker();

  Future getImage() async {
    final image = await picker.getImage(source: ImageSource.gallery);
    if (image != null && image.path != null) {
      File rotatedImage =
          await FlutterExifRotation.rotateImage(path: image.path);

      if (image != null) {
        setState(() {
          _image = rotatedImage;
        });
      }
    }
  }

  Future getImageAndSave() async {
    final image = await picker.getImage(source: ImageSource.gallery);
    if (image != null && image.path != null) {
      File rotatedImage =
          await FlutterExifRotation.rotateAndSaveImage(path: image.path);

      if (image != null) {
        setState(() {
          _image = rotatedImage;
        });
      }
    }
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
        persistentFooterButtons: <Widget>[
          new FloatingActionButton(
            onPressed: getImageAndSave,
            tooltip: 'Pick Image and save',
            child: new Icon(Icons.save),
          ),
          new FloatingActionButton(
            onPressed: getImage,
            tooltip: 'Pick Image without saving',
            child: new Icon(Icons.add),
          ),
        ],
      ),
    );
  }
}
