package com.cliffex.flutter_exif_rotation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterExifRotationPlugin */
public class FlutterExifRotationPlugin implements  MethodCallHandler, PluginRegistry.RequestPermissionsResultListener {
  private final Registrar registrar;

  private Result result;
  private MethodCall call;

  private final PermissionManager permissionManager;


  static final int REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION = 2344;



  interface PermissionManager {
    boolean isPermissionGranted(String permissionName);

    void askForPermission(String[] permissions, int requestCode);
  }

  public FlutterExifRotationPlugin(Registrar registrar, final Activity activity) {

    this.registrar = registrar;

    permissionManager = new PermissionManager() {
      @Override
      public boolean isPermissionGranted(String permissionName) {
        return ActivityCompat.checkSelfPermission(activity, permissionName)
                == PackageManager.PERMISSION_GRANTED;
      }

      @Override
      public void askForPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
      }

    };
  }

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_exif_rotation");
    FlutterExifRotationPlugin flutterExifRotationPlugin = new FlutterExifRotationPlugin(registrar, registrar.activity());
    channel.setMethodCallHandler(flutterExifRotationPlugin);
    registrar.addRequestPermissionsResultListener(flutterExifRotationPlugin);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    this.result = result;
    this.call = call;
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("rotateImage")) {
      rotateImage();
    } else {
      result.notImplemented();
    }
  }

  @Override
  public boolean onRequestPermissionsResult(
          int requestCode, String[] permissions, int[] grantResults) {
    boolean permissionGranted =
            grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    switch (requestCode) {
      case REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION:
        if (permissionGranted) {
          if (this.call != null)
            launchRotateImage();
          return true;
        }
        break;

      default:
        return false;
    }

    if (!permissionGranted) {
      return false;
    }

    return true;
  }


  public void rotateImage() {

    if (!permissionManager.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ||
            !permissionManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      Log.w("rotateImage","rotate image request permission");
      permissionManager.askForPermission(new String[]{
                      Manifest.permission.READ_EXTERNAL_STORAGE,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE},
              REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION);

      return;
    }
    launchRotateImage();

  }

  public void launchRotateImage() {
    String photoPath = call.argument("path");
    Boolean save = call.argument("save");

    int orientation = 0;
    try {
      ExifInterface ei = new ExifInterface(photoPath);
      orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
              ExifInterface.ORIENTATION_UNDEFINED);


      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inPreferredConfig = Bitmap.Config.ARGB_8888;

      Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

      Bitmap rotatedBitmap = null;
      switch (orientation) {

        case ExifInterface.ORIENTATION_ROTATE_90:
          rotatedBitmap = rotate(bitmap, 90);
          break;

        case ExifInterface.ORIENTATION_ROTATE_180:
          rotatedBitmap = rotate(bitmap, 180);
          break;

        case ExifInterface.ORIENTATION_ROTATE_270:
          rotatedBitmap = rotate(bitmap, 270);
          break;

        case ExifInterface.ORIENTATION_NORMAL:
        default:
          rotatedBitmap = bitmap;
      }

      File file = new File(photoPath); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

      FileOutputStream fOut = new FileOutputStream(file);

      rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
      fOut.flush(); // Not really required
      fOut.close(); // do not forget to close the stream

      if( save )
        MediaStore.Images.Media.insertImage(registrar.activity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

      result.success(file.getPath());
    } catch (IOException e) {
      Log.w("rotateImage error",e.getMessage()+"");
      result.error("error", "IOexception", null);
      e.printStackTrace();
    }

  }

  private static Bitmap rotate(Bitmap source, float angle) {
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true);
  }

}
