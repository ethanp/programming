# 4/21/14
# Ethan Petuchowski
# "Find best performing hospital in given state on given measure"

best <- function (state, outcome) {
    
    outcomes <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
    
    # Check that the state is valid
    if (!state %in% outcomes$State) 
        stop("invalid state")
    
    # figure out which column contains the data we want
    col_we_want <- 
        if (outcome == "heart attack") {         11
        } else if (outcome == "heart failure") { 17
        } else if (outcome == "pneumonia") {     23
        } else { stop("invalid outcome")
        }
    
    # get outcomes for specified state
    o <- outcomes[outcomes$State == state,]
    
    # cast column to numeric type
    o[,col_we_want] <- as.numeric(o[,col_we_want])

    # sort state's table by outcome, then hospital name
    s <- o[order(o[,col_we_want], o[,2]),]
    
    # return hospital name on top of the sorted list
    s$Hospital.Name[1]
}
