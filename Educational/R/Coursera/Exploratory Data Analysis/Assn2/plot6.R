# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot6.R

## The Question

# 6. Compare emissions from motor vehicle sources in Baltimore City with
#    emissions from motor vehicle sources in Los Angeles County, California
#    (fips == "06037").
#
#    Which city has seen greater changes over time in motor vehicle emissions?

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')

if (!exists("LA_data"))
    LA_data <- NEI[NEI$fips == "06037",]

# the year is a factor variable, so it's just a line to sum it up
if (!exists("LA_by_year"))
    LA_by_year <- aggregate(LA_data$Emissions, by=list(year=LA_data$year), FUN=sum)


plot6 <- function() {
    # do the same as plot2 for LA County
    # plot them both on the same plot
    # add a label showing the different differences over time between the cities
    
    merged <- merge(LA_by_year, balt_by_year, by='year')
    merged <- rename(merged, c('x.y' = 'Baltimore City', 'x.x' = 'Los Angeles County'))
    
    library(reshape)
    dat <- melt(merged, id.vars = 'year')
    
    
    q <- ggplot(data = dat, aes(year, value, color = variable))
    q <- q + geom_point()
    q <- q + scale_color_manual(values = c('red', 'blue'))
    q <- q + opts(title = 'Emissions in Baltimore vs LA')
    q <- q + ylab(expression('PM2.5 Emissions (tons)'))
    q <- q + geom_smooth(method = 'lm', fill=NA)
    q <- q + coord_cartesian(ylim = c(0, 50000))
    q
    ggsave(file="plot6.png", dpi = 100)
}

