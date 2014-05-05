# 5/4/14

files <- list.files("/Users/Ethan/Downloads/afm_k/")
frames <- list()

for (file in files) {
    a <- read.delim(file, header = FALSE, sep = " ", 
                    colClasses = c("character", "character", "numeric"))

    # TODO I think this combines the data.frames into one
    # list of columns, but I want to keep them separate
    frames <- append(frames, d)
}
