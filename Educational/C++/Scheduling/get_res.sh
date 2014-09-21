function g() {
    grep milli r${1}${2}.txt > g${1}${2}.txt
}

function h() {
    g $1 0
    g $1 $1
    g $1 $(($1 + 1))
}

h 4
h 8

