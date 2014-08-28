//
//  main.cpp
//  HelloWorld
//
//  Created by Ethan Petuchowski on 8/26/14.
//

#include <iostream>

/*
 * a very basic class
 */
class Something {
    int len;
public:
    int getLen();
    void setLen(int i){len=i;}
};

/*
 * a very basic example of inheritance
 */
class SomethingElse : public Something {
public:
    int getDoubleLen() {
        return 2 * this->getLen();
    }
};


/*
 * not sure when defining something out here would ever be useful...
 */
int Something::getLen() {
    return this->len;
}

/*
 * trying everything out
 */
int main(int argc, const char * argv[])
{
    std::cout << "Hello, World!\n";
    
    /* if we declare like this (no new!?) we don't need the *
     * stars or the arrows.                                 *
     * I don't yet understand how this all works.           *
     * I also don't know yet /which is better/.             */
    Something a;
    a.setLen(3);
    std::cout << a.getLen() << '\n';
    
    SomethingElse *b = new SomethingElse;
    b->setLen(4);
    std::cout << b->getDoubleLen() << '\n';
    
    return 0;
}

