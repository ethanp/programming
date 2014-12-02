#include <iostream>
#include "handle.h"

using namespace std;

int main() {
    cout << "Hello, World!" << endl;
    circle c(3);
    triangle t(3, 4);
    cout << c.area() << endl << t.area() << endl;
    return 0;
}
