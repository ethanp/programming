# Coursera Exploratory Data Analysis Project 1
# Plot 1
# red histogram of (Global Active Power (kilowatts), Frequency)
# "base" plotting system
# 5/5/14
# Ethan Petuchowski

setwd('/Users/ethan/code/non_apple/coursera/exploratory-data-analysis/ExData_Plotting1')
power_consumption <- '/Users/ethan/code/non_apple/data/household_power_consumption.txt'

plot1 <- function (data_loc = power_consumption) {
                                                  
    # Read raw data
    data <- read.csv(data_loc, 
                     sep = ';', 
                     na.strings = c('?'),  # save '?' in raw data as 'NA' value
                     colClasses = c(rep('character', 2), rep('numeric', 7)),
                     comment.char = '', 
                     nrows = 69518)        # only read data we need

    
    # only plot date range we care about
    filtered <- subset(data, Date == '1/2/2007' | Date == '2/2/2007')

    
    # construct and save histogram
    
    png('plot1.png') # open PNG device
    
    hist(filtered$Global_active_power,
         col  = 'red',
         main = 'Global Active Power',
         xlab = 'Global Active Power (kilowatts)')
    
    dev.off() # save PNG to filesystem
}
