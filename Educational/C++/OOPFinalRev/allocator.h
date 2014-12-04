/* this should use NESTED CLASSES just 'cause that's another thing on his list of things to know
    yeesh I can't remember this stuff AT ALL!
    how about creating a linked-list with an allocator that uses nested classes?
    surely this will take a few hours */

#include <memory>  /* allocator */
#include <cstddef> /* size_t */

/* TODO how do I make this into a proper ALLOCATOR? */
template <typename T>
class linked_list_allocator {
public:
    struct node {
        T item;
        node* next;
    };

    /* allocator methods */
    T* allocate(size_t n) {
        for (size_t i = 0; i < n; i++) {

        }
        return nullptr;
    }
    void construct(T* p, const T& v) {
    }
    void deallocate(T* p, size_t s) {
    }
    void destroy(T* p) {
    }
private:
    node head;
};

template <typename AllocT, typename ArrT>
void my_destroy(AllocT& alloc, ArrT arr, ArrT arrEnd) {
    while (arr != arrEnd)
        alloc.destroy(&*(arr++));
}

template <typename AllocT, typename ToFillT, typename ValT>
void my_uninitialized_fill(AllocT& alloc, ToFillT first, ToFillT end, ValT value) {
    ToFillT copied_ptr = first;
    try {
        while (copied_ptr != end)
            alloc.construct(&*(copied_ptr++), value);
    } catch (...) {
        my_destroy(alloc, first, copied_ptr);
        throw;
    }
}

template <typename T, typename A = std::allocator<T> >
class my_vector {
public:
    my_vector(std::size_t sz = 0, const T& v = T(), const A& a = A()) :
            the_allocator(a), arr_size(sz), _arr(the_allocator.allocate(sz))
    {
        my_uninitialized_fill(the_allocator, _arr, _arr+sz, v);
    }

    T& operator [](std::size_t idx) const {
            return _arr[idx];
    };

    ~my_vector() {
        my_destroy(the_allocator, _arr, _arr+arr_size);
        the_allocator.deallocate(_arr, arr_size);
    }

private:
    A the_allocator;
    std::size_t arr_size;
    T* const _arr;
};
