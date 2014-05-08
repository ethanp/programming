# 5/7/14
# Ethan Petuchowski
# Coursera -- Exploratory Data Analysis
# Course Project 2
# readData.R


## This first line will likely take a few seconds. Be patient!

# data frame with emissions data for '99,'02,'05,'08
NEI <- readRDS('/Users/ethan/code/non_apple/data/exdata-data-NEI_data/summarySCC_PM25.rds')

# mapping from SCC digit strings to name of particulate matter's source
SCC <- readRDS('/Users/ethan/code/non_apple/data/exdata-data-NEI_data/Source_Classification_Code.rds')

