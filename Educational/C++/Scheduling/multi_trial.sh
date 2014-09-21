function run() {
    ./multi $1 $2 | tee r${1}${2}.txt
}
function over() {
    run $1 0
    run $1 $1
    run $1 $(($1 + 1))
}

over 4
over 8
