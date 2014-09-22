function run() {
    ./aff_multi $1 | tee raw_aff${1}.txt
}

run 0
run 8
run 9
