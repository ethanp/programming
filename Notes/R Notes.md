# R Commands and Plotting Cheat Sheet

### Started April 21, 2014

### Data Frames

##### Getting rows that match a criterion

	# TFAE
	subset(data, mpg > 30 & hp > 100)
	data[data$mpg > 30 & data$hp > 100]
	
##### Getting all unique values of a column
	
	d <- unique(dataframe$Column)

##### Sort by one column, then another

	sorted <- df[order(df[,colNum1], df$colName2),]   # trailing comma!
	
##### Remove ALL rows that have an NA in ANY column

	df <- df[complete.cases(df),]
	
##### Remove rows with NAs in (set of) column(s)

	df <- df[complete.cases(df[,colNum]),]  # trailing comma!
	
##### Get last 2 rows

	t <- tail(dataframe, 2)
	
##### Construct data frame with two given vectors as its columns

	data.frame(name1=vector1, name2=vector2)
	
##### Cast a column to numeric

	df[,colNum] <- as.numeric(df[,colNum])
	
##### Read from csv with "character" data type for every column

	data <- read.csv("data.csv", colClasses = "character")

##### View column names, types, and first few elements

	str(dataframe)

##### Get vector of column names

	names(df)


### Vectors

##### Check if vector contains given element

	if (!string %in% data$Strings)
		stop("invalid string")  # essentially "raise exception"

### Factor variables

##### Locate it in a dataframe

	> str(reddit)
	'data.frame':	32754 obs. of  14 variables:
	$ id               : int  1 2 3 4 5 6 7 8 9 10 ...
 	$ gender           : int  0 0 1 0 1 0 0 0 0 0 ...
	$ employment.status: *Factor* w/ 6 levels "Employed full time",..: 1 1 2 2 ...

##### Print breakdown table by count
	
	> table(reddit$employment.status)

    Employed  	Freelance 	Unemp. not looking    Unemp. looking    Retired    Student
     14814       1948          682                  2087              85         12987 

##### Plot histogram of above table

	library(ggplot2)
	qplot(data = dataframe, x = factor.var)

##### Get vector of levels

    > levels(reddit$employment.status)
	[1] "Employed"  "Freelance"  "Not...not" "Not...but"   
    [5] "Retired"  "Student"

##### Turn other type of vector into Factor vector

	factor(vector)

### Plotting (req's library(ggplot2) )

#### Multiple plots in one plot

##### Using ggplot2

[R Cookbook](http://www.cookbook-r.com/Graphs/Multiple_graphs_on_one_page_(ggplot2)/)
(You have to copy/paste the `multiplot()` function they have there.)

	p1 <- qplot(...)
	p2 <- qplot(...)
	...
	multiplot(p1, p2, ..., cols=2)

##### Using StdLib

Column of three

	par(mfrow=c(2,2)) # or whatever dimensions you want
	attach(mtcars)    # make columns into global vars ?
	hist(wt)          # plot the three histograms
	hist(mpg)
	hist(disp)
	
One figure in row 1 and two figures in row 2

	attach(mtcars)
	layout(matrix(c(1,1,2,3), 2, 2, byrow = TRUE))
	hist(wt)
	hist(mpg)
	hist(disp)

One figure in `row 1` and two figures in `row 2`. `row 1` is
1/3 the *height* of `row 2`, `column 2` is 1/4 the *width* of
`column 1`

	attach(mtcars)
	layout(matrix(c(1,1,2,3), 2, 2, byrow = TRUE), 
	       widths=c(3,1), heights=c(1,2))
	hist(wt, main='wt')
	hist(mpg, main='mpg')
	hist(disp, main='disp')

#### Labels

	qplot(..., xlab='x-axis', ylab='y-axis', main='this is the title)

##### Turn axis labels 90 degrees

	q + theme(axis.text.x=element_text(angle=-90))
	
##### Set tick limits & interval

	q + scale_y_continuous(breaks=seq(min, max, interval))
	
##### Legend

Set name

	qplot(mpg, wt, data=mtcars, color=factor(cyl)) + labs(color="Legend-Name")

Remove legend

	qplot(mpg, wt, data=mtcars, color=factor(cyl)) +
		  theme(legend.position='none')
		 
Move legend

	qplot(mpg, wt, data=mtcars, colour=factor(cyl), geom="point") +
		 opts(legend.position="left")

#### Histograms

##### Plot histogram of factor variable's count-table

Given that you have `dataframe$factor.var`, you can say

	qplot(factor.var, data=dataframe)
	
##### Histogram of Factor var, with another factor var setting sub-colors

This uses one of the standard datasets `diamonds`

	qplot(clarity, data=diamonds, fill=cut)
	
##### Rotate it 90 degrees to sideways

	qplot(factor(cyl), data=mtcars) + coord_flip()
	
##### Color bars by factor

	qplot(factor(cyl), data=mtcars, fill=factor(gear))
	# position="stack" (this is the default)
	# setting 'color' instead doesn't work
	
##### Histogram of factor, separated by 2nd factor var

	qplot(clarity, data=diamonds, fill=cut, position="dodge")

##### Histogram of 2 Factors, fill to 100%

	qplot(clarity, data=diamonds, fill=cut, position="fill")

##### Similar to "dodge" above, but seen as line graph

	qplot(clarity, data=diamonds, geom="freqpoly", group=cut, colour=cut, position="identity")
	
##### Set binwidth

	qplot(carat, data=diamonds, binwidth=0.01)
	
#### Scatterplots

##### Scatterplot of 2 Numeric vars within dataframe

	qplot(nvar1, nvar2, data=df)
	
##### Use 3rd Numeric var as colorscale/alpha/size

	qplot(wt, mpg, data=mtcars, color=qsec)
	qplot(wt, mpg, data=mtcars, alpha=qsec)
	qplot(wt, mpg, data=mtcars, size=qsec)

##### Use 3rd Factor var as shape

	qplot(wt, mpg, data=mtcars, shape=factor(cyl))

##### Add that steamy trendline and standard error

	# 'point' is just the normal, 'smooth' is just the steam
	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'))
	
###### Split them up by factor

	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), color=factor(cyl))
	
###### Remove standard error

	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), se = FALSE)
	
###### Adjust line's wiggliness

	# set span between .1 and 1
	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), span=0.6)
	
###### Use a linear fit

	qplot(wt, mpg, data=mtcars, geom=c("point", "smooth"), method="lm")

##### Add straight lines

###### Horizontal (two of them here)

	qplot(...) + geom_vline(xintercept=c(-3,3), linetype="dotted")

###### Vertical

	qplot(...) + geom_hline(yintercept = value)
	
There's also a **`geom_abline()`**, which seems more generic.
For instance, you can give it  a slope.

### Commands

##### Ternary operator

	df$v <- ifelse(cond, "light", "average")
	
##### Read TSV

	tsv <- read.csv('name.tsv', sep = '\t')
	
#### Database-like operations

##### Group-By Cross Product

**This could come in real handy**

	SELECT count(*) FROM diamonds GROUP BY clarity, cut

	# ?plyr: The plyr package is a set of clean and consistent
	# tools that implement the split-apply-combine pattern in R.
	library(plyr)
	
	# ?ddply: For each subset of a data frame,
	# apply function then combine results into a data frame.
	t.tbl <- ddply(diamonds, c('clarity','cut'), 'nrow')
	
##### More Group-By

	diamonds[DataFrame]
	diamonds$cut[OrderedFactor]
	summarize[vector => vector]
	meanDepth[col we're creating]
	diamonds$depth[Numeric]

	SELECT mean(depth) FROM diamonds GROUP BY cut

	ddply(diamonds, 'cut', summarize, meanDepth = mean(depth))
	
	
	SELECT 25%ile(depth), median(depth), 75%ile(depth) FROM diamonds GROUP BY cut
	
	> ddply(diamonds, "cut", summarize, lower = quantile(depth, 0.25, +
	    na.rm=TRUE), median = median(depth, na.rm=TRUE), +
	    upper = quantile(depth, 0.75, na.rm=TRUE))
	    
	        cut lower median upper
	1      Fair  64.4   65.0  65.9
	2      Good  61.3   63.4  63.8
	3 Very Good  60.9   62.1  62.9
	4   Premium  60.5   61.4  62.2
	5     Ideal  61.3   61.8  62.2
