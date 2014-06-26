# 6/24/14
# this came from Amitesh's gist
# https://gist.github.com/Amitesh/1247229

require 'rubygems'
require 'zip/zip'

def unzip_file (file, destination)
  Zip::ZipFile.open(file) { |zip_file|
   zip_file.each { |f|
     f_path = File.join(destination, f.name)
     FileUtils.mkdir_p(File.dirname(f_path))
     zip_file.extract(f, f_path) unless File.exist?(f_path)
   }
  }
end

# I guess you have to use expand_path to use "~" for "/Users/myName"
unzip_file(File.expand_path('~/Desktop/a.zip'), File.expand_path('~/Desktop/abc'))
