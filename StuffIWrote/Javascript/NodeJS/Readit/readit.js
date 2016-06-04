// ALL fields are OPTIONAL (for flexibility)

var Something = function(title, text, globalID, references, assignees, parentID) {
    this.title = title ||  "";
    this.text = text ||  "";
    this.globalID = globalID ||  "";
    this.references = references ||  [];
    this.assignees = assignees ||  [];
    this.parentID = parentID ||  "";
}

Something.prototype.firstFunction = function(message) {
    console.log("firstFunction says: "+message);
}

