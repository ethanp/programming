var fs = require('fs');
fs.readdir(process.argv[2], function(err, list) {
    list.forEach(function(name) {
        if (name.endsWith('.'+process.argv[3])) {
            console.log(name);
        }
    });
});
