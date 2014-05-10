# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot2.R

## The Question

# 2. Have total emissions from PM2.5 decreased in the Baltimore City,
#    Maryland (fips == "24510") from 1999 to 2008?
#
#    Use the BASE plotting system to make a plot answering this question.


## this is code we want to run whenever this file is Sourced
## so it gets saved as an environment variable that can be inspected

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')

if (!exists("baltimore_data"))
    baltimore_data <- NEI[NEI$fips == "24510",]

# the year is a factor variable, so it's just a line to sum it up
if (!exists("balt_by_year"))
    balt_by_year <- aggregate(baltimore_data$Emissions, by=list(year=baltimore_data$year), FUN=sum)


plot2 <- function() {
    # filter data to Baltimore, then do something similar to plot1
    
    png('plot2.png') # open PNG device
    
    # now scatter plot emmissions by year
    plot(balt_by_year, ylim=c(0, 3.5E3), ylab='PM2.5 emmisions (tons)',
         main='Total US PM2.5 emmisions 1999 to 2008', xaxt='n')
    axis(1, at = balt_by_year$year)
    
    # and add a linear model
    fit <- lm(balt_by_year$x ~ balt_by_year$year)
    abline(fit)
    
    dev.off() # save PNG to filesystem
    
}
