# 4/21/14
# Ethan Petuchowski
# rankall.R
# "returns a 2-column data frame containing the hospital
# in each state that has the ranking specified in num."

rankall <- function (outcome, num = "best") {
    outcomes <- read.csv("outcome-of-care-measures.csv", colClasses = "character")
    
    # figure out which column contains the data we want
    col_we_want <-     
        if (outcome == "heart attack")       11
        else if (outcome == "heart failure") 17
        else if (outcome == "pneumonia")     23
        else stop("invalid outcome")
    
    names <- character()
    
    states <- unique(outcomes$State)
    
    # get sorted list of states
    states <- states[order(states)]
    
    # for each state
    for (state in states) {
        
        # get outcomes for specified state
        o <- outcomes[outcomes$State == state,]
        
        # cast column to numeric type
        o[,col_we_want] <- as.numeric(o[,col_we_want])
        
        # sort state's table by outcome, then hospital name
        s <- o[order(o[,col_we_want], o[,2]),]
        
        # remove NAs
        s <- s[complete.cases(s[,col_we_want]),]
        
        # find the hospital of the given rank
        if (num == "best") num <- 1
        
        h <- if (num == "worst") 
                 tail(s,1)$Hospital.Name 
             else 
                 s$Hospital.Name[num]
        
        # append it to the list
        names <- append(names, h)
    }
    
    # return a data frame with the hospital names and the state abbr.
    data.frame(hospital=names, state=states)
}
