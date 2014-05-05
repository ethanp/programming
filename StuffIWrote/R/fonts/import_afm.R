# 5/4/14

setwd("/Users/Ethan/Downloads/afm/")
files <- list.files('.')

frames <- list()

for (file in files) {
    
    # TODO read the columns as (text,text,text,number)
    a <- read.delim(file, header = FALSE, sep = " ")
    b <- a[a$V1 == 'KPX',]
    c <- b[,c(1,2,3,4)]
    
    # turn factors (by default) into numerics
    c$V4 <- as.numeric(as.character(c$V4))
    
    # not sure this is necessary
    d <- as.data.frame(c)

    # TODO I think this combines the data.frames into one
    # list of columns, but I want to keep them separate
    frames <- append(frames, d)
}
