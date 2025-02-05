package io.flutter.plugins.flutterexifrotation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * FlutterExifRotationPlugin
 */
class FlutterExifRotationPlugin : FlutterPlugin, MethodCallHandler {
    private var applicationContext: Context? = null
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = binding.applicationContext
        val methodChannel = MethodChannel(binding.binaryMessenger, channelName)
        methodChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        runOnBackground {
            when (call.method) {
                "rotateImage" -> {
                    launchRotateImage(call, result)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun launchRotateImage(call: MethodCall, result: MethodChannel.Result) {
        val photoPath = call.argument<String>("path")
        val save = argument(call, "save", false)!!
        val orientation: Int
        try {
            val ei = ExifInterface(photoPath!!)

            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(photoPath, options)
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            val file =
                File(photoPath) // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            val fOut = FileOutputStream(file)
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush() // Not really required
            fOut.close() // do not forget to close the stream
            if (save) {
                MediaStore.Images.Media.insertImage(
                    applicationContext?.contentResolver,
                    file.absolutePath,
                    file.name,
                    file.name
                )
            }
            result.success(file.path)
        } catch (e: IOException) {
            result.error("error", "IOexception", null)
            e.printStackTrace()
        } catch (e: NullPointerException) {
            result.error("bitmap_decoding_error", "Bitmap could not be decoded: ${e.message}", null) 
            e.printStackTrace()
        }
    }

    companion object {
        private const val channelName = "flutter_exif_rotation"

        val threadPool: ExecutorService = Executors.newSingleThreadExecutor()

        inline fun runOnBackground(crossinline block: () -> Unit) {
            threadPool.execute {
                block()
            }
        }

        private fun rotate(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }

        private fun <T> argument(call: MethodCall?, key: String, defaultValue: T): T? {
            return if (!call!!.hasArgument(key)) {
                defaultValue
            } else call.argument(key)
        }
    }
}