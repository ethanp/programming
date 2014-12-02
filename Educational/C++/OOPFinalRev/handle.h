#include <iostream>

class shape_handle {

};

class abstract_shape {
public:
    virtual double area() = 0;
    virtual abstract_shape* clone() = 0;
    friend std::ostream& operator << (std::ostream& o, abstract_shape& a);
};

/* default inheritance access is private for classes */
class circle : public abstract_shape {
public:
    circle(int _r) : r(_r) {}
    virtual double area() { return 3.142*r*r; }
    virtual circle* clone() { return new circle(*this); } /*copy const*/
    friend std::ostream& operator << (std::ostream& o, circle& c) {
        return o << "Circle w radius: " << c.r
                 << " and area: " << c.area() << '\n';
    }
private:
    int r;
};

class triangle : public abstract_shape {
public:
    triangle(int _b, int _h) : b(_b), h(_h) {}
    virtual double area() { return (static_cast<double>(b)*h)/2; }
    virtual triangle* clone() { return new triangle(*this); }
    friend std::ostream& operator << (std::ostream& o, triangle& t) {
        return o << "Triangle w (b,h): (" << t.b << "," << t.h
                 << ") and area: " << t.area() << '\n';
    }
private:
    int b, h;
};
