package io.flutter.plugins.flutterexifrotation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterExifRotationPlugin
 */
public class FlutterExifRotationPlugin implements FlutterPlugin, MethodCallHandler, PluginRegistry.RequestPermissionsResultListener, ActivityAware {

    private Result result;
    private MethodCall call;
    private MethodChannel methodChannel;

    private Activity activity;

    private PermissionManager permissionManager;

    static final int REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION = 23483;

    interface PermissionManager {
        boolean isPermissionGranted(String permissionName);

        void askForPermission(String[] permissions, int requestCode);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_exif_rotation");
        FlutterExifRotationPlugin flutterExifRotationPlugin = new FlutterExifRotationPlugin();
        channel.setMethodCallHandler(flutterExifRotationPlugin);
        registrar.addRequestPermissionsResultListener(flutterExifRotationPlugin);
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel = new MethodChannel(binding.getBinaryMessenger(), "flutter_exif_rotation");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull final ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
        permissionManager = new PermissionManager() {
            @Override
            public boolean isPermissionGranted(String permissionName) {
                return ActivityCompat.checkSelfPermission(activity, permissionName) == PackageManager.PERMISSION_GRANTED;
            }

            @Override
            public void askForPermission(String[] permissions, int requestCode) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }

        };
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // TODO: the Activity your plugin was attached to was
        // destroyed to change configuration.
        // This call will be followed by onReattachedToActivityForConfigChanges().
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull final ActivityPluginBinding activityPluginBinding) {
        activity = activityPluginBinding.getActivity();
        // TODO: your plugin is now attached to a new Activity
        // after a configuration change.
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
        // TODO: your plugin is no longer associated with an Activity.
        // Clean up references.
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull Result result) {
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
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION:
                if (permissionGranted) {
                    if (this.call != null) {
                        launchRotateImage();
                    }
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
        Boolean internalFile = argument(call, "internalFile", false);

        if (!internalFile
                && (!permissionManager.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                || !permissionManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

            permissionManager.askForPermission(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_IMAGE_STORAGE_PERMISSION
            );

            return;
        }
        launchRotateImage();

    }

    private void launchRotateImage() {
        String photoPath = call.argument("path");
        Boolean save = argument(call, "save", false);

        int orientation = 0;
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
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

            if (save) {
                MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            }

            result.success(file.getPath());
        } catch (IOException e) {
            result.error("error", "IOexception", null);
            e.printStackTrace();
        } finally {
            call = null;
            result = null;
        }

    }

    private static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static <T> T argument(MethodCall call, String key, T defaultValue) {
        if (!call.hasArgument(key)) {
            return defaultValue;
        }

        return call.argument(key);
    }
}
