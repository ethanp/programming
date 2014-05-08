# Coursera Exploratory Data Analysis Project 1
# Plot 4
# 4 separate linegraphs of various energy monitoring data
# over Thu to Sat, including plots 2 and 3
# "base" plotting system
# 5/5/14
# Ethan Petuchowski

power_consumption <- '/Users/ethan/code/non_apple/data/household_power_consumption.txt'

# initial rough estimate
plot4 <- function (data_loc = power_consumption) {
    
    # Read raw data
    data <- read.csv(data_loc, sep = ';', na.strings = c('?'),
                     colClasses = c(rep('character', 2), rep('numeric', 7)),
                     comment.char = '', nrows = 69518)
    
    # only plot date range we care about
    filtered <- subset(data, Date == '1/2/2007' | Date == '2/2/2007')
    
    len <- length(filtered$Global_active_power)
    
    png('plot4.png') # open PNG device
    
    # allocate a 2x2 grid of plots
    par(mfrow = c(2,2))
    
    # put the plots on the grid
    with(data, {
        plot(filtered$Global_active_power, type = 'l', xlab = '', ylab = 'Global Active Power', xaxt = 'n')
        axis(side = 1, at = c(1, len/2, len), labels = c('Thu', 'Fri', 'Sat'))
        plot(filtered$Voltage, type = 'l', xlab = 'datetime', ylab = 'Voltage', xaxt = 'n')
        axis(side = 1, at = c(1, len/2, len), labels = c('Thu', 'Fri', 'Sat'))
        plot(filtered$Sub_metering_1, type = 'l', xlab = '', ylab = 'Energy sub metering', xaxt = 'n')
        lines(filtered$Sub_metering_2, col = 'red', type = 'l')
        lines(filtered$Sub_metering_3, col = 'blue', type = 'l')
        axis(side = 1, at = c(1, len/2, len), labels = c('Thu', 'Fri', 'Sat'))
        legend('topright', c('Sub_metering_1', 'Sub_metering_2', 'Sub_metering_3'),
               col = c('black', 'red', 'blue'), lty = c(1,1,1), box.lwd = 0)
        plot(filtered$Global_reactive_power, type = 'l', xlab = 'datetime', ylab = 'Global_reactive_power', xaxt = 'n')
        axis(side = 1, at = c(1, len/2, len), labels = c('Thu', 'Fri', 'Sat'))
    })
    
    dev.off() # save PNG to filesystem
}
