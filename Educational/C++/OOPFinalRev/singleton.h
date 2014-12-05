#include <iostream>
#include <string>

/* these are based-on/taken-from
    github.com/gpdowning/cs371p/blob/master/exercises/Singleton.c++ */

class singleton1 {
public:
    static singleton1& get() { return obj; }
    std::string method_on_obj() { return "called method on obj\n"; }
private:
    singleton1(const singleton1&){} /* remove copy constructor */
    singleton1(){} /* you can't instantiate the thing */
    static singleton1 obj;
};

/* apparently, the problem with this one is
   it goes on the Heap, which is not ideal. */
class singleton2 {
public:
    static singleton2& get() {
        /* like in the good-ol' Stanford obj-c days */
        if (!obj) obj = new singleton2(/*NB*/); return *obj; }
private:
    singleton2(const singleton2&){} /* remove copy constructor */
    singleton2(){} /* you can't instantiate the thing */

    /* since it is a pointer it is not initialized when the class is created
     * it waits for us to fill-it-in at some point (which we do with `new`) */
    static singleton2* obj;
};

class singleton3 {
public:
    static singleton3& get() { static singleton3 obj; return obj; }
private:
    singleton3(const singleton3&){} /* remove copy constructor */
    singleton3(){} /* you can't instantiate the thing */
};
