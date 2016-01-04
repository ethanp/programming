console.log(
    process.argv.slice(2).reduce(
        (prev, curr) => prev + parseInt(curr), 0
    )
);
