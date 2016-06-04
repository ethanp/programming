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

var program = require('commander');
program
    .arguments('<file>')
    .option('-u, --username <username>', 'The user to authenticate as')
    .option('-p, --password <password>', 'The user\'s password')
    .action(function(file) {
    console.log('user: %s pass: %s file: %s',
       program.username, program.password, file);
    })
    .parse(process.argv);
