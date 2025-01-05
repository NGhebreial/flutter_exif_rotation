import Flutter
import UIKit

public class SwiftFlutterExifRotationPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_exif_rotation", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterExifRotationPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (call.method == "getPlatformVersion") {
            result("iOS " + UIDevice.current.systemVersion)
        } else if (call.method == "rotateImage") {
            guard let args = call.arguments else {
                result("iOS could not recognize flutter arguments in method: (sendParams)")
                return
            }
            let imagePath = ((args as AnyObject)["path"]! as? String)!
            let outputPath = (((args as AnyObject)["outputPath"] as? String?) ?? imagePath)!
            let image = UIImage(contentsOfFile: imagePath)
            
            if let updatedImage = image?.updateImageOrientationUpSide() {
                
                let fileManager = FileManager.default
                let imageData = updatedImage.jpegData(compressionQuality: 0.8); fileManager.createFile(atPath: outputPath, contents: imageData, attributes: nil)
                result(outputPath)
            } else {
                result(outputPath)
            }
        }
    }
}
// Image extension
extension UIImage {
    func updateImageOrientationUpSide() -> UIImage? {
        if self.imageOrientation == .up {
            return self
        }
        
        UIGraphicsBeginImageContextWithOptions(self.size, false, self.scale)
        self.draw(in: CGRect(x: 0, y: 0, width: self.size.width, height: self.size.height))
        if let normalizedImage:UIImage = UIGraphicsGetImageFromCurrentImageContext() {
            UIGraphicsEndImageContext()
            return normalizedImage
        }
        UIGraphicsEndImageContext()
        return nil
    }
}
