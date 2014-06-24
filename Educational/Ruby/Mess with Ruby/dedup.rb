# Ethan Petuchowski
# 6/23/14
# Remove duplicate lines from csv while keeping header
# This really is a nice language

File.open('sample_out.txt', 'w+') do |file|
  file.puts(File.read('sample_in.txt').split("\n").uniq)
end
