#include <iostream>
#include "handle.h"

using namespace std;

int main() {

    circle c(3);
    triangle t(3, 4);
    cout << c << t;

    shape_handle<abstract_shape> s(new circle(5));
    cout << s;
    shape_handle<abstract_shape> s2 = s;
    shape_handle<abstract_shape> s3(new triangle(5, 6));
    cout << s2;
    cout << boolalpha;
    cout << (s == s2) << '\n';
    cout << s3;
    s3 = s;
    cout << s3;
    return 0;
}
