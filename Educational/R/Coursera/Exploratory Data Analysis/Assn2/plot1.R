# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot1.R

## The Question

# 1. Have total emissions from PM2.5 decreased in the United States
#    from 1999 to 2008?
#
#    Using the base plotting system, make a plot showing the total
#    PM_2.5 emission from all sources for each of the years 1999,
#    2002, 2005, and 2008.

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')

# scatterplot of summed output for each year for which we have data
plot1 <- function() {
    
    # the year is a factor variable, so it's just a line to sum it up
    if (!exists("emm_by_year"))
        emm_by_year <- aggregate(NEI$Emissions, by=list(year=NEI$year), FUN=sum)
    
    png('plot1.png') # open PNG device
    
    # now scatter plot emmissions by year
    plot(emm_by_year, ylim=c(0, 7.5E6), ylab='PM2.5 emmisions (tons)',
         main='Total US PM2.5 emmisions 1999 to 2008', xaxt='n')
    axis(1, at = emm_by_year$year)
    
    # and add a linear model
    fit <- lm(emm_by_year$x ~ emm_by_year$year)
    abline(fit)
    
    dev.off() # save PNG to filesystem
}
