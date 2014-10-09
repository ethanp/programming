#include <iostream>
using namespace std;
int g = 0;
class A {
public:
  A()               { cout << ++g << ": " << "A() "     << endl; }
  ~A()              { cout << ++g << ": " << "~A() "    << endl; }
  A(const A& other) { cout << ++g << ": " << "A(copy) " << endl; }
  virtual void f()  { cout << ++g << ": " << "A::f() "  << endl; }
};

class B : public A {
public:
  B() : A() { cout << ++g << ": " << "B() "     << endl; }
  ~B()      { cout << ++g << ": " << "~B() "    << endl; }
  void f()  { cout << ++g << ": " << "B::f() "  << endl; }
};

void construct() {
  cout << ++g << ": " << "construct()" << endl;
  A a[5] = { B(), B(), B() };
  a[1].f();
  cout << ++g << ": " << "~construct()" << endl;
}

void construct_heap() {
  cout << ++g << ": " << "construct_heap()" << endl;
  A *a[] = { new B(), new B(), new B() };
  a[1]->f();
  // delete [] a;   // NO!
  for (int i = 0; i < 3; i++)
    delete a[i];
  cout << ++g << ": " << "~construct_heap()" << endl;
}

int main(int argc, char const *argv[]) {
  construct();
  construct_heap();
  return 0;
}
