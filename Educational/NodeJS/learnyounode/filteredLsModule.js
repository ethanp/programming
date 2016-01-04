var fs = require('fs');
module.exports = function(dirname, ext, cb) {
    fs.readdir(dirname, function(err, list) {
        if (err) return cb(err);
        var toRet = list.filter(function(filename) {
            return filename.endsWith('.'+ext);
        });
        cb(null, toRet)
    })
};
