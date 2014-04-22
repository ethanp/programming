# R Cheat Sheet

### April 21, 2014

##### Getting the rows of a data frame that match a criterion

	# TFAE
	subset(data, mpg > 30 & hp > 100)
	data[data$mpg > 30 & data$hp > 100]
	
##### Getting all unique values of a column
	
	d <- unique(dataframe$Column)

##### Sort data frame by one column, then another

	sorted <- df[order(df[,colNum1], df$colName2),]   # trailing comma!
	
##### Remove ALL rows that have an NA in ANY column

	df <- df[complete.cases(df),]
	
##### Remove rows with NAs in (set of) column(s)

	df <- df[complete.cases(df[,colNum]),]  # trailing comma!
	
##### Get last 2 rows of data frame

	t <- tail(dataframe, 2)
	
##### Construct data frame with two given vectors as its columns

	data.frame(name1=vector1, name2=vector2)
	
##### Cast data frame column to numeric

	df[,colNum] <- as.numeric(df[,colNum])
	
##### Read csv with "character" data type for every column

	data <- read.csv("data.csv", colClasses = "character")
	
##### Check if vector contains given element

	if (!string %in% data$Strings)
		stop("invalid string")  # essentially "raise exception"
