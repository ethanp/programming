#include <iostream>
#include "handle.h"

using namespace std;

int main() {

    circle c(3);
    triangle t(3, 4);
    cout << c << t;

    shape_handle<abstract_shape> s(new circle(5));
    cout << s;
    // TODO test the assignment operator and copy constructor
    return 0;
}
