## 0.6.0

* BREAKING CHANGE: Android and iOS now have the same behavior for saving the fixed file. Both overwrite the existing one by default.
* Added argument to allow specifying an output path to save the fixed image to instead of overwriting the existing one.

## 0.5.2
* Upgraded gradle by @javaddehban

## 0.5.1
* Fixing OutOfMemory exception when rotating multiple images by @mZadorskii

## 0.5.0

* Migration to Kotlin implemented by @paricleu
* SDK min version to 2.14.0

## 0.4.3

* Fixed permissions to make it work when tested for the first time

## 0.4.2

* Added use without permissions by @antondudakov

## 0.4.1

* Buxfix #38

## 0.4.0

* Upgraded image_picker to 0.7.2 and dart to 2.12 by @dvaldivia
* Using null safety

## 0.3.2

* iOS podspec deployment target set to 8.0

## 0.3.1

* iOS podspec

## 0.3.0

* Plugin migration to work with Android v2 embedding by @itsmejohndoe

## 0.2.8

* Removed unnecessary prints by @Setti7
* Return error if permissions are denied by @lColinDl
* Fixes images overwriting when using multiple image picker on iOS by @bkoznov
* Removed pubspec.lock to match Dart guidelines by @freitzzz
* Upgraded image_picker package

## 0.2.7

* Some iOS fix 

## 0.2.6

* Some iOS fix 

## 0.2.5

* Added iOS code made by @Bhagatcliffex 

## 0.2.4

* Added new method that returns the file without saving it in the gallery

## 0.2.3

* Fixed bug that makes the app crash when deny the permissions 

## 0.2.2

* Fixed the way of request the permissions

## 0.2.1

* Fixed bug on permissions

## 0.2.0

* Migrate to AndroidX on android support library. 
If you are using the android support library and you want to use that version, you should migrate to Androidx your project. 

## 0.1.1

* Some changes on readme.md

## 0.1.0

* Initial release