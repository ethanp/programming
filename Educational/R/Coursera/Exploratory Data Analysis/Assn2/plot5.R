# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot5.R

## The Question

# 5. How have emissions from motor vehicle sources changed from 1999–2008
#    in Baltimore City?

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')

plot5 <- function() {
    # this is probably data.category == onroad
    
    balt_on_road <- baltimore_type_year[baltimore_type_year$type == 'ON-ROAD',]
    
    # use baltimore_type_year
    q <- qplot(year, Emissions, data = balt_on_road,
               ylab='Emissions (tons)', 
               main='Emissions from motor vehicles in Baltimore')
    
    # add linear model
    q <- q + geom_smooth(method = 'lm', fill=NA)
    q <- q + geom_smooth(fill=NA, color = 'grey')
    q
    ggsave(file="plot5.png")
}
