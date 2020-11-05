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
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'

  s.ios.deployment_target = '8.0'
  s.swift_version = '5.3'
end

