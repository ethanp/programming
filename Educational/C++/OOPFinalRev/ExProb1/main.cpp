#include <iostream>
#include "birds.h"

using namespace std;

int main() {
    cout << "Hello, World!" << endl;

    /* these came from gist.github.com/Zin20/2c341d6dcc6656933a4f */
    bird* x1 = new redJay("redJay",2);
    assert(x1->what_to_Eat() == "grass");
    assert(x1->where_to_Sleep() == "bush");
    assert(x1->fly()=="it flew!");
    assert(x1->Defecate() == "crap");

    bird* x2 = new blueJay("blueJay",2);
    assert(x2->what_to_Eat()=="water");
    assert(x2->where_to_Sleep() == "flames");
    assert(x2->fly()=="it flew!");
    assert(x2->Defecate() == "crap");


    assert(!((*x2)==(*x1)));
    *(x2) = *(x1);
    assert((*x2) == (*x1));

    //dodo x3 = dodo::inst();
    assert(dodo::inst().fly() == "it doesn't" );
    assert(&(dodo::inst()) == &(dodo::inst()));
    assert(dodo::inst().what_to_Eat() == "nothing");
    assert(dodo::inst().where_to_Sleep() == "bottom of a valley");

    //bird* x3 = new dodo(); doesn't work because constructor is private
    x2 = new blueJay("blueJay",2);
    birdHandler x4 (x2);
    assert(x4->fly() == "it flew!");
    assert(x4->what_to_Eat() == "water");
    assert(x4->where_to_Sleep() == "flames");
    birdHandler x5(x4);
    //assert(&x5 == &x4);
    assert(x5->what_to_Eat() == "water");
    assert(x5->where_to_Sleep() == "flames");
    x5.change();
    assert(x5->what_to_Eat() == "grass");
    assert(x5->where_to_Sleep() == "bush");
    std::cout << "success!\n";
    return 0;
}
