setwd('/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/fonts')

tenUp <- read.csv(file = '10AndUpData.csv')
colnames(tenUp) <- c('font', 'item', 'value')
tenUp$item <- as.factor(tenUp$item)

plot(cut$item, cut$value)

plot(tenUp$item, tenUp$value)

set.seed(10)
cut <- split(tenUp, sample(rep(1:3, length(tenUp)/3)))[[1]]
cut <- split(cut, sample(rep(1:3, length(cut)/3)))[[1]]
