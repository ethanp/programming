# 4/20/14

complete <- function(directory, id = 1:332) {
    oldwd <- getwd()
    setwd(directory)

    nobs <- numeric()

    for (i in id) {      # build list of filenames
        f <- paste(sprintf("%03d", i), ".csv", sep="") # concat strings
        data <- read.csv(f, header=TRUE)    # metadata improves speed
        data <- data[complete.cases(data),] # remove NAs
        nobs <- append(nobs, nrow(data))
    }
    d <- data.frame(ids=id, nobs=nobs)
    setwd(oldwd)
    d
}
