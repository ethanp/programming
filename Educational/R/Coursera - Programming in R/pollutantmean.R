# 4/20/14

pollutantmean <- function(directory, pollutant, id = 1:332) {
    oldwd <- getwd()
    setwd(directory) # go where the files live

    files <- character() # empty character vector
    for (i in id) {      # build list of filenames
        f <- paste(sprintf("%03d", i), ".csv", sep="")
        files <- append(files, f)
    }
    data <- do.call("rbind", lapply(files, read.csv)) # read the files in
    data <- data[complete.cases(data),] # ignore NAs
    poll_data <- data[[pollutant]]      # select the correct pollutant
    setwd(oldwd)                        # return home
    mean(poll_data)                     # return the desired mean
}
