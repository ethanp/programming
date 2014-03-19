Commands to Command
-------------------

### dirname

    $ dirname a/b/myfile
    a/b

    $ dirname a/myfile
    a

    $ dirname myfile
    .


### basename

    $ basename "./dir space/other dir/file.txt"
    file.txt

    # DON'T do this by accident (viz. ALWAYS quote the filename)
    $ basename ./KF Audio/9_20832/KF Audio/FakeSerialNumber-08_00_29_Test_Set/Test_Summary_DB_List.txt
    KF
    KF
    Test_Summary_DB_List.txt

### find

    $ find .
    # recursive listing of all the files with names like
    ./.DS_STORE
    ./file.txt
    ./a
    ./a/anotherFile.txt
    ./a space b
    ./a space b/file.txt

### read

[linuxcommand.org](http://linuxcommand.org/wss0110.php)

    echo -n "Enter some text > "
    read text
    echo "You entered: $text"

    Enter some text > this is some text
    You entered: this is some text

