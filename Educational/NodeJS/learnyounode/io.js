var fs = require('fs');
var fileBuff = fs.readFileSync(process.argv[2]);
var txt = fileBuff.toString();
var lines = txt.split('\n');
console.log(lines.length-1);
