# 4/21/14
# Ethan Petuchowski
# rankhospital.R

rankhospital <- function (state, outcome, num = "best") {
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
    
    # remove NAs
    s <- s[complete.cases(s[,col_we_want]),]
    
    if (num == "best") num <- 1
    if (num == "worst") {
        last <- tail(s,1)
        last$Hospital.Name
    } else {
        s$Hospital.Name[num]
    }
}
