# Coursera Exploratory Data Analysis Project 1
# Plot 2
# linegraph of Global Active Power (kilowatts) from Thu to Sat
# "base" plotting system
# 5/5/14
# Ethan Petuchowski

power_consumption <- '/Users/ethan/code/non_apple/data/household_power_consumption.txt'

plot2 <- function (data_loc = power_consumption) {
    
    # Read raw data
    data <- read.csv(data_loc, sep = ';', na.strings = c('?'),
                     colClasses = c(rep('character', 2), rep('numeric', 7)),
                     comment.char = '', nrows = 69518)
    
    # only plot date range we care about
    filtered <- subset(data, Date == '1/2/2007' | Date == '2/2/2007')
   
    png('plot2.png') # open PNG device
    
    # construct line graph
    plot(filtered$Global_active_power, type = 'l',
         ylab = 'Global Active Power (kilowatts)',
         xlab = '', xaxt = 'n') # don't plot default x-axis
    
    len <- length(filtered$Global_active_power)
    
    # add x-axis
    axis(side = 1, at = c(1, len/2, len), labels = c('Thu', 'Fri', 'Sat'))
    
    dev.off() # save PNG to filesystem
}
