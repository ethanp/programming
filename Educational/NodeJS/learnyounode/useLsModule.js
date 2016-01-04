require('./filteredLsModule')(
    process.argv[2],
    process.argv[3],
    function(err, data) {
        if (err) return console.error('error: ', err);
        data.forEach(function(filename) {
            console.log(filename);
        });
    }
);
