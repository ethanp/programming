# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot3.R

## The Question

# 3. Of the four types of sources indicated by the type (point, nonpoint,
#    onroad, nonroad) variable, which of these four sources have seen
#    decreases in emissions from 1999–2008 for Baltimore City?
#
#    Which have seen increases in emissions from 1999–2008?
#
#    Use the ggplot2 plotting system to make a plot answer this question.

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')
library(ggplot2)


if (!exists("baltimore_data"))
    baltimore_data <- NEI[NEI$fips == "24510",]

# group by two factor variables at once (type & year)
if (!exists('baltimore_type_year')) {
    baltimore_type_year <- aggregate(Emissions ~ type + year, data = baltimore_data, sum)
}

plot3 <- function() {
    
    #### THIS IS A GROUPED-LINEAR MODEL VERSION OF THE SAME THING ####
    
    # plot each type in a different color
    # plot linear models over year, by type
    q <- qplot(year, Emissions, 
               data=baltimore_type_year,
               color=type)
    q <- q + geom_smooth(method = 'lm', fill=NA)
    q <- q + coord_cartesian(ylim = c(0, 2200))
    q <- q + geom_point(size = 3)
    q
    
    
    
    #### THIS IS THE PICTURED GROUPED-HISTOGRAM VERSION ####
    
    q <- ggplot(baltimore_type_year, aes(factor(year), Emissions, fill=type))
    q <- q + geom_bar(stat='identity', position = 'dodge')
    q <- q + scale_fill_brewer(palette='Set1')
    q
    
    ggsave(file="plot3B.png", dpi = 100)
    
}
