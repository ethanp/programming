#include <exception>
#include <iostream>

struct My_exception : std::exception {
  const char* what() const throw () {
    return "oh how could you!";
  }
};

int f(bool b) {
  if (b)
    throw My_exception();
  return 0;
}

int main() {
  try {
    f(false) == 0; // that’s fine
  } catch (My_exception& e) {/* won’t happen */}
  try {
    f(true) == 0; // uh oh…
  } catch (My_exception& e) {
    std::cout << e.what() << std::endl;
  }
}
