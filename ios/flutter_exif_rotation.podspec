#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'flutter_exif_rotation'
  s.version          = '0.3.0'
  s.summary          = 'Flutter plugin that fixes the picture orientation'
  s.description      = <<-DESC
    Flutter plugin that fixes the picture orientation when taken in landscape for some devices.
                       DESC
  s.homepage         = 'https://github.com/NGhebreial/flutter_exif_rotation'
  s.license          = { :type => 'BSD' }
  s.author           = { 'Nadia Ghebreial' => 'nadiagnieto@gmail.com' }
  s.source           = { :git => 'https://github.com/NGhebreial/flutter_exif_rotation.git', :tag => 'v0.3.0' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '9.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
