latex input:        mmd-article-header
Title:              R Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Statistics, Data Science, Coursera, Machine Learning
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
Copyright:          2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

### Started April 21, 2014

# Statistics

## Linear Regression

### Fit a regression line

	x <- c(0.8, 0.47, 0.51, 0.73, 0.36, 0.58, 0.57, 0.85, 0.44, 0.42)
	y <- c(1.39, 0.72, 1.55, 0.48, 1.19, -1.59, 1.23, -0.65, 1.49, 0.05)
	fit <- lm(y ~ x)

Produces:

	Coefficients:
	(Intercept)           x
	1.567       		-1.713

# Loading Data

## Downloading data with curl
**6/2/14**

	# Create data dir
	setwd("/my/project/location")

	# list.files()  # to make sure it worked

	if (!file.exists("data")) { dir.create("data") }

	# list.files()  # to make sure it worked

	# Download data
	fileUrl <- "https://data.csv"
	download.file(fileUrl, destfile = "./data/data.csv", method = "curl") # curl for https

	# Record the date you downloaded it (now you can put that in the readme)
	dateDownloaded <- date()

**6/2-4/14**

## Delimited Values

### read.table/csv()

#### Parameters

* **file** -- the default first parameter
* **header** -- `TRUE/FALSE`
* **sep** -- default for *table* = `'\t'`, for *csv* = `','`
* **row.names**
* **nrows** -- how many rows of the file to read
* **quote** -- whether there are quoted values (`quote=""` may resolve issues with quotes in the data)
* **na.strings** -- what characters represent a missing value
* **skip** -- number of lines to skip before starting to read

## Excel

### read.xlsx

* It's in the **xlsx package** -- `library(xlsx)`
* **read.xlsx2()** -- faster more "unstable version" of the same thing
* **file** -- first parameter
* **sheetIndex** -- e.g. `1`
* **header** -- `TRUE/FALSE`
* **colIndex** -- e.g. `2:3`
* **rowIndex** -- e.g. `1:4`

### XLConnect

* Another package with a lot more options for writing and manipulating Excel files
* Has its own nice documentation, look up *XLConnect Vignette*

## XML (Extensible Markup Language)

* In the **XML package** -- `library(XML)` (all caps)

### Parse it into a tree in memory

	doc <- xmlTreeParse(fileUrl, useInternal=TRUE)

### Get the root node

	rootNode <- xmlRoot(doc)

### Get the node name (within the tag)

	xmlName(rootNode)

### Get an element of a node

	rootNode[[1]] # first element

	rootNode[[1]][[1]] # first elem of first elem

### Apply to all elements

#### E.g. recursively loop through all nodes from root, and get their values

	xmlSApply(rootNode, xmlValue)

### Use XPath

* `/node` -- top level
* `//node` -- any level

E.g. get all values within `<name>` tags

	xpathSApply(rootNode, "//name", xmlValue)

## HTML

### Similar to XML

E.g. get all `<li class=score>234</li>`

	doc <- htmlTreeParse(fileUrl, useInternal=TRUE)
	scores <- xpathSApply(doc, "//li[@class='score']", xmlValue)

## JSON

* In the **jsonlite package** -- `library(jsonlite)`

### Read file

	jsonData <- fromJSON(theUrl)

### Get the attribute names

	names(jsonData)

### Specific to object *within* object

	names(jsonData$owner)

Drill down even further

	names(jsonData$owner$login)

### Write out tod JSON

	myjson <- toJSON(iris, pretty=TRUE)

## MySQL

	library(RMySQL)

**Note:** this works for non MySQL db's too.

    # Open connection to db
    ucscDb <- dbConnect(MySQL(), user="genome", host="genome-mysql.cse.ucsc.edu")

    # Send command through connection
    # this gets a vector of all the available databases from this host
    result <- dbGetQuery(ucscDb, "show databases;")

	# Disconnect when you're done
	dbDisconnect(ucscDb)

	# Connect to a particular database
	hg19 <- dbConnect(MySQL(), user="genome", db="hg19", host="genome-mysql.cse.ucsc.edu")

	# Get a vector of all tables within database
	allTables <- dbListTables(hg19)

	# Get fields of specific table within connected database
	dbListFields(hg19, "affyU133Plus2")

	# Run query on specific connected database table
	dbGetQuery(hg19, "select count(*) from affyU133Plus2")

	# Retrieve data from table into dataframe
	affyData <- dbReadTable(hg19, "affyU133Plus2")

	# Run query on database, not locally (I think that's the difference)
	query <- dbSendQuery(hg19, "select * from affyU133Plus2 where misMatches between 1 and 3")

	# Retrieve results of that query into dataframe
	affyMis <- fetch(query)

	# Limit number of results retrieved
	affyMisSmall <- fetch(query, n = 10)

	# Clear query when you're done
	dbClearResult(query)

## HDF5

* Used for storing large, heirarchically structured data from a range of data types.
* Created for use with [literal] supercomputers
* *groups* contain zero or more data sets and metadata
	* *group header* -- group name and list of attributes
	* *group symbol table* -- list of objects in the group
* *datasets* -- multidimensional array of data elements with metadata
	* *header* -- name, type, dataspace, storage layout
	* *data array* -- holds the data

Here's how you download it:

	source("http://bioconductor.org/biocLite.R")
	biocLite("rhdf5")
	library(rhdf5)
	created = h5createFile("example.h5")

I'm going to stop right there though because I don't know if I'll *ever* need this.


## Data from the web

### Web Scraping

	con = url("http://theUrl.com/dataLocation")
	htmlCode = readLines(con)
	close(con)
	htmlCode

#### Using the httr package

	library(httr); html2 = GET(url)
	content2 = content(html2,as="text")
	
	# parse the html
	parsedHtml = htmlParse(content2,asText=TRUE)
	
	# get the titles
	xpathSApply(parsedHtml, "//title", xmlValue)
	
	# or whatever you want
	xpathSApply(html, "//td[@id='col-citedby']", xmlValue)
	
#### When you need to authenticate

	pg2 = GET("http://httpbin.org/basic-auth/user/passwd", authenticate("user","passwd"))
	
#### Using a handle

This allows you to save the authentication between times you use the handle

	google = handle("http://google.com")
	pg1 = GET(handle=google,path="/")
	pg2 = GET(handle=google,path="search")


### Web APIs

Demos can be found at `https://github.com/hadley/httr/blob/master/demo/`

#### Using OAuth

	myapp = oauth_app("twitter", key="yourConsumerKeyHere",secret="yourConsumerSecretHere")
	sig = sign_oauth1.0(myapp, token = "yourTokenHere", token_secret = "yourTokenSecretHere")
	
	# ask api for statuses from my home timeline
	homeTL = GET("https://api.twitter.com/1.1/statuses/home_timeline.json", sig)

#### Convert retrieved JSON to a dataframe

	# extract json data
	json1 = content(homeTL)
	
	# reformat it as dataframe
	json2 = jsonlite::fromJSON(toJSON(json1))

# Data Frames

#### **NOTE** data.table is much faster (but different) than data.frame

#### Getting rows that match a criterion

	# TFAE
	subset(data, mpg > 30 & hp > 100)
	data[data$mpg > 30 & data$hp > 100]

#### Getting all unique values of a column

	d <- unique(dataframe$Column)

#### Sort by one column, then another

	sorted <- df[order(df[,colNum1], df$colName2),]   # trailing comma!

#### Remove ALL rows that have an NA in ANY column

	df <- df[complete.cases(df),]

#### Remove rows with NAs in (set of) column(s)

	df <- df[complete.cases(df[,colNum]),]  # trailing comma!

#### Get last 2 rows

	t <- tail(dataframe, 2)

#### Construct data frame with two given vectors as its columns

	data.frame(name1=vector1, name2=vector2)

#### Cast a column to numeric

	df[,colNum] <- as.numeric(df[,colNum])

#### Read from csv with "character" data type for every column

	data <- read.csv("data.csv", colClasses = "character")

#### View column names, types, and first few elements

	str(dataframe)

#### Get vector of column names

	names(df)

#### Apply a summary function to each column of data frame

E.g. apply the `min()` function, to collapse each column into its minimum value

	minima <- sapply(am, min, na.rm=TRUE)

**Returns** e.g. Length 7 vector if there were 7 columns


# Programming in R

## Vectors

#### Check if vector contains given element

	if (!string %in% data$Strings)
		stop("invalid string")  # essentially "raise exception"

## Factor variables

#### Locate it in a dataframe

	> str(reddit)
	'data.frame':	32754 obs. of  14 variables:
	$ id               : int  1 2 3 4 5 6 7 8 9 10 ...
 	$ gender           : int  0 0 1 0 1 0 0 0 0 0 ...
	$ employment.status: *Factor* w/ 6 levels "Employed full time",..: 1 1 2 2 ...

#### Print breakdown table by count

	> table(reddit$employment.status)

    Employed  	Freelance 	Unemp. not looking    Unemp. looking    Retired    Student
     14814       1948          682                  2087              85         12987

#### Plot histogram of above table

	library(ggplot2)
	qplot(data = dataframe, x = factor.var)

#### Get vector of levels

    > levels(reddit$employment.status)
	[1] "Employed"  "Freelance"  "Not...not" "Not...but"
    [5] "Retired"  "Student"

#### Turn other type of vector into Factor vector

	factor(vector)

## Plotting (req's library(ggplot2) )

### Multiple plots in one plot

#### Using ggplot2

[R Cookbook](http://www.cookbook-r.com/Graphs/Multiple_graphs_on_one_page_(ggplot2)/)
(You have to copy/paste the `multiplot()` function they have there.)

	p1 <- qplot(...)
	p2 <- qplot(...)
	...
	multiplot(p1, p2, ..., cols=2)

#### Using Base

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

### Labels

	qplot(..., xlab='x-axis', ylab='y-axis', main='this is the title)

#### Turn axis labels 90 degrees

	q + theme(axis.text.x=element_text(angle=-90))

#### Set tick limits & interval

	q + scale_y_continuous(breaks=seq(min, max, interval))

### Legend

#### Set name

	qplot(mpg, wt, data=mtcars, color=factor(cyl)) + labs(color="Legend-Name")

#### Remove legend

	qplot(mpg, wt, data=mtcars, color=factor(cyl)) +
		  theme(legend.position='none')

#### Move legend

	qplot(mpg, wt, data=mtcars, colour=factor(cyl), geom="point") +
		 opts(legend.position="left")

### Histograms

#### Plot individual histograms for each column of data_frame

[SO source](http://stackoverflow.com/questions/13035834)

Given that you've already removed all columns you don't want to plot with `df[,c(...)]`

    plot_histogram_of_data_frame <- function (in_data_frame) {
        library(reshape)
        library(ggplot2)
    	  d <- melt(in_data_frame)
    	  ggplot(d,aes(x = value)) +
             facet_wrap(~variable,scales = "free_x") +
             geom_histogram()
    }



#### Plot histogram of factor variable's count-table

Given that you have `dataframe$factor.var`, you can say

	qplot(factor.var, data=dataframe)

#### Histogram of Factor var, with another factor var setting sub-colors

This uses one of the standard datasets `diamonds`

	qplot(clarity, data=diamonds, fill=cut)

#### Rotate it 90 degrees to sideways

	qplot(factor(cyl), data=mtcars) + coord_flip()

#### Color bars by factor

	qplot(factor(cyl), data=mtcars, fill=factor(gear))
	# position="stack" (this is the default)
	# setting 'color' instead doesn't work

#### Histogram of factor, separated by 2nd factor var

	qplot(clarity, data=diamonds, fill=cut, position="dodge")

#### Histogram of 2 Factors, fill to 100%

	qplot(clarity, data=diamonds, fill=cut, position="fill")

#### Similar to "dodge" above, but seen as line graph

	qplot(clarity, data=diamonds, geom="freqpoly", group=cut, colour=cut, position="identity")

#### Set binwidth

	qplot(carat, data=diamonds, binwidth=0.01)

### Scatterplots

#### Scatterplot of 2 Numeric vars within dataframe

	qplot(nvar1, nvar2, data=df)

#### Use 3rd Numeric var as colorscale/alpha/size

	qplot(wt, mpg, data=mtcars, color=qsec)
	qplot(wt, mpg, data=mtcars, alpha=qsec)
	qplot(wt, mpg, data=mtcars, size=qsec)

#### Use 3rd Factor var as shape

	qplot(wt, mpg, data=mtcars, shape=factor(cyl))

#### Add that steamy trendline and standard error

	# 'point' is just the normal, 'smooth' is just the steam
	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'))

##### Split them up by factor

	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), color=factor(cyl))

##### Remove standard error

	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), se = FALSE)

##### Adjust line's wiggliness

	# set span between .1 and 1
	qplot(wt, mpg, data=mtcars, geom=c('point', 'smooth'), span=0.6)

##### Use a linear fit

	qplot(wt, mpg, data=mtcars, geom=c("point", "smooth"), method="lm")

#### Add straight lines

##### Horizontal (two of them here)

	qplot(...) + geom_vline(xintercept=c(-3,3), linetype="dotted")

##### Vertical

	qplot(...) + geom_hline(yintercept = value)

There's also a **`geom_abline()`**, which seems more generic.
For instance, you can give it  a slope.


## Commands

### Ternary operator

	df$v <- ifelse(cond, "light", "average")


### Retrieve standard output of UNIX command

	datafiles <- system('find . | grep FileSuffix.txt', intern=TRUE)

### Database-like operations

#### Group-By Cross Product

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

### Strings

#### String substitution

**Note:** You must double-escape

	serial <- sub("\\./(\\w+)(.*)","\\1", f)
