rm -f otfs.log
cd 2src
make clean hello
cd ..
fusermount -u b
2src/hello a b
cd a
rm -f foo
echo "asdf" > foo
cd ../b
./a.out
c() { xxd -p "$1" | tr -d '\n' | cut -c-10; } # just load the function
echo
if [ `c foo` -eq "0951417130" ]; then
    echo "foo is legit"
else
    echo "problem with file foo"
fi
cd ..
echo
