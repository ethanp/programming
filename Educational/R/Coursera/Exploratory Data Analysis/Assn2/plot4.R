# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# plot4.R

## The Question

# 4. Across the United States, how have emissions from coal
#    combustion-related sources changed from 1999–2008?

setwd('/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Exploratory Data Analysis/Assn2')

plot4 <- function() {
    
    # find SCCs that have to do with coal
    coal_codes <- SCC[grepl("Coal",SCC$Short.Name),]
    
    # only keep NEIs that have to do with coal
    coal_neis <- NEI[NEI$SCC %in% coal_codes$SCC,]
    
    # aggregate by year
    by_year <- aggregate(coal_neis$Emissions,
                         by  = list(year=coal_neis$year),
                         FUN = sum)
    
    # make into scatter plot
    q <- qplot(year, x, data = by_year,
               ylab='Emissions (tons)', 
               main='Emissions from coal in the US')
    
    # add linear model
    q <- q + geom_smooth(method = 'lm', fill=NA)
    q <- q + coord_cartesian(ylim = c(0, 650000))
    q <- q + geom_point(size = 3)
    q
    
    ggsave(file="plot4.png")
    
}
