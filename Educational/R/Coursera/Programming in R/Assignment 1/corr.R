# 4/20/14

corr <- function(directory, threshold = 0) {
    oldwd <- getwd()
    setwd(directory)

    toc <- numeric()

    for (f in list.files()) {      # build list of filenames
        data <- read.csv(f, header=TRUE)    # metadata improves speed
        data <- data[complete.cases(data),] # remove NAs

        if (nrow(data) > threshold) {
            toc <- append(toc, cor(data$nitrate, data$sulfate))
        }
    }
    setwd(oldwd)
    toc
}
