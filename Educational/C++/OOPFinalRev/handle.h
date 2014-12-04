#include <iostream>

template <typename T>
class shape_handle {
    typedef T* pointer;
public:
    shape_handle(pointer p) : shape(p) {}

    /* copy constructor clones "that's" underlying shape object */
    shape_handle(const shape_handle& h) {
        if (!h.shape) shape = 0; /* don't try to ->clone() a nullptr! */
        else shape = h.shape->clone();
    }
    bool operator == (const shape_handle& h) {
        if (!shape && !h.shape) return true;
        if (!shape || !h.shape) return false;
        return *shape == *h.shape;
    }
    shape_handle& operator = (shape_handle s) { swap(s); return *this; }
    void swap(shape_handle& h) { std::swap(shape, h.shape); }
    ~shape_handle() { delete shape; }
    T& operator * () const { return *shape; }

    friend std::ostream& operator << (std::ostream& o, const shape_handle& s) {
        return o << "Shape handle containing: " << *s.shape;
    }
protected:
    pointer shape;
};

class abstract_shape {
public:
    virtual double area() = 0;
    virtual abstract_shape* clone() = 0;
    friend std::ostream& operator << (std::ostream& o, abstract_shape& a) {
        return a.write(o);
    }
    friend bool operator == (const abstract_shape& a, const abstract_shape& b) {
        return a.equals(b);
    }
    virtual std::ostream& write(std::ostream& o) = 0;
    virtual ~abstract_shape() {};
    virtual bool equals(const abstract_shape& a) const {
        /* there would be stuff in here if this class had any private fields */
        return true;
    }
};

/* default inheritance access is private for classes */
class circle : public abstract_shape {
public:
    circle(int _r) : r(_r) {}
    circle(const circle& c) : r(c.r) {}
    virtual double area() { return 3.142*r*r; }
    virtual circle* clone() { return new circle(*this); } /*copy const*/
    virtual std::ostream& write(std::ostream& o) {
        return o << "Circle w radius: " << r
                 << " and area: " << area() << '\n';
    }
    virtual bool equals(const abstract_shape& a) {
        if (const circle* const p = dynamic_cast<const circle*>(&a))
            return abstract_shape::equals(*p) && r == p->r;
        return false;
    }
private:
    int r;
};

class triangle : public abstract_shape {
public:
    triangle(int _b, int _h) : b(_b), h(_h) {}
    triangle(const triangle& t) : b(t.b), h(t.h) {}
    virtual double area() { return (static_cast<double>(b)*h)/2; }
    virtual triangle* clone() { return new triangle(*this); }
    virtual std::ostream& write(std::ostream& o) {
        return o << "Triangle w (b,h): (" << b << "," << h
                 << ") and area: " << area() << '\n';
    }
    virtual bool equals(const abstract_shape& a) {
        if (const triangle* const p = dynamic_cast<const triangle*>(&a))
            return abstract_shape::equals(*p) && b == p->b && h == p->h;
        return false;
    }
private:
    int b, h;
};
