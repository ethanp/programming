#include <iostream>
#include <cassert>
#include <algorithm>

/*
Problem 1:

Birds come in all sizes and types.

Write an abstract class called bird
that takes in its type and the size of it (string and int)
With the following methods:

    fly()
    what_to_Eat()
    where_to_Sleep()
    Defecate()

Write two children of bird redJay and blueJay.
Red Jays like to eat grass and sleep in a bush.
Blue Jays like to eat water and they sleep in flames.
Both birds can fly and defecate.

A science project has gone horrible wrong and
scientists have been able to bring back one dodo.
Dodo's can't fly, they eat nothing, and they sleep at a bottom of a valley.
Write a class dodo based on these 100% true science facts.
Method inst() should return the instance of that dodo.

birdHandlers are able to take care of blue and red jays, but not the one dodo.
Write birdHandler such that the birdhandlers can do between blue and red jays.
Also write a method called change that forces the birdHandler to have a red jay.
 */

/* abstract base class */
class bird {
public:
    bird(std::string _name, int _size) : name(_name), size(_size) {}
    virtual ~bird(){}
    virtual std::string fly()           =0;
    virtual std::string what_to_Eat()   =0;
    virtual std::string where_to_Sleep()=0;
    virtual std::string Defecate()      =0;
    virtual bird* clone()               =0;
    friend bool operator == (const bird& b, const bird& c) {
        return b.equals(c);
    }
    virtual bool equals(const bird& b) const {
        return name == b.name && size == b.size;
    }
private:
    std::string name; int size;
};

class redJay : public bird {
public:
    redJay(std::string name, int size) : bird(name, size) {}

    virtual std::string fly()           { return "it flew!"; }
    virtual std::string what_to_Eat()   { return "grass"; }
    virtual std::string where_to_Sleep(){ return "bush"; }
    virtual std::string Defecate()      { return "crap"; }
    virtual redJay* clone()             { return new redJay(*this); }
};

class blueJay : public bird {
public:
    blueJay(std::string name, int size) : bird(name, size) {}
    virtual std::string fly()           { return "it flew!"; }
    virtual std::string what_to_Eat()   { return "water"; }
    virtual std::string where_to_Sleep(){ return "flames"; }
    virtual std::string Defecate()      { return "crap"; }
    virtual blueJay* clone()            { return new blueJay(*this); }
};

class dodo : public bird {
public:
    static dodo& inst() {
        static dodo loc("dodo", 2);
        return loc;
    }
    dodo(std::string name, int size) : bird(name, size) {}
    virtual std::string fly()           { return "it doesn't"; }
    virtual std::string what_to_Eat()   { return "nothing"; }
    virtual std::string where_to_Sleep(){ return "bottom of a valley"; }
    virtual std::string Defecate()      { return "crap"; }
    virtual dodo* clone()               { return new dodo(*this); }
};

/* handler class */
class birdHandler {
public:
    birdHandler(bird* birdptr) : bird_ptr(birdptr) {}
    ~birdHandler() { delete bird_ptr; }
    birdHandler(const birdHandler& other) {
        if (!other.bird_ptr) bird_ptr = 0;
        else bird_ptr = other.bird_ptr->clone();
    }
    birdHandler& operator = (birdHandler other) {
        swap(other); return *this;
    }
    void change() {
        if (const redJay* p = dynamic_cast<redJay*>(bird_ptr)) {
            delete bird_ptr;
            bird_ptr = new blueJay("blueJay", 3);
        } else if (const blueJay* q = dynamic_cast<blueJay*>(bird_ptr)) {
            delete bird_ptr;
            bird_ptr = new redJay("redJay", 3);
        } else std::cout << "something went wrong\n";
    }
    bird& operator * () { return *bird_ptr; }
    bird* operator -> () { return bird_ptr; }
private:
    void swap(birdHandler& bh) { std::swap(bird_ptr, bh.bird_ptr); }
    bird* bird_ptr;
};
