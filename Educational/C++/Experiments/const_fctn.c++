#include <iostream>
class A {
public:
  void foo() const { std::cout << "A::foo() const\n"; }
  void foo() { std::cout << "A::foo()\n"; }
};

int main() {
  A bar = A();
  const A cbar = A();
  bar.foo();  // calls foo
  cbar.foo(); // calls foo const
}
