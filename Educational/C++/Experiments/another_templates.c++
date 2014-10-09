/** Ethan Petuchowski
 *  10/9/14
 *  equal, copy, fill, count, etc.
 */
#include <iostream>
#include <vector>
#include <cassert>

/*count:
    returns the number of incidences of x in a range
    count( begin, end, x)
*/
template <typename Iter, typename T>
int my_count(Iter begin, Iter end, const T& val) {
    int ctr = 0;
    while (begin != end)
        if (*begin++ == val)
            ctr ++;
    return ctr;
}

/*equal:
    returns true if everything between first1- end1 and first2-end1 are the same
    equal( first of iterator 1, end iterator 1, first of iterator 2)
    first 1 and end 1 are same type; first 2 is different type
*/
template <typename Iter1, typename Iter2>
bool my_equal(Iter1 start, Iter1 end, Iter2 other) {
    while (start != end)
        // this is supposed to use the == not the != op for some reason
        if (!(*start++ == *other++))
            return false;
    return true;
}

/*fill:
    returns void
    fill(iterator first, iterator last,T& val)
    assigns val to all elements in range [first, last) (eg *first = val)
    first two parameters are initial & final positions in a
    sequence to be assigned the value of the third parameter
*/
template <typename I, typename T>
void my_fill(I start, I end, const T& val) {
    while (start != end)
        *start++ = val;
}

/*find:
    returns either beginning of iterator or end of iterator that was given
    find( first iterator 1, end iterator 1, value )
    first 1 and end 1 are same type, val is different type
    returns type of first1 and end1
    loops until first 1 = end 1, inclusive (incrementing first 1)
    if *current = val return first
    if never found, return end
*/
template <typename I, typename T>
I my_find(I start, I end, const T& val) {
    while (start != end) {
        if (*start == val) {
            return start;
        } else {
            start++;
        }
    }
    return end;
}

/*remove:
   returns iterator without element val
   remove( first iterator 1, end iterator 1, value )
   first 1 and end 1 are same type, val is different type
   loops until first 1 = end 1, inclusive
   makes a new iterator of first and last and if *current != val, copy to new iterator
   after loop, returns new iterator
*/
template <typename I, typename T>
I my_remove(I start, I end, const T& val) {
    I other = start; // this is the one we're copying into
    while (start != end) {
        if (!(*start == val))
            *other++ = *start; // clever...
        start++;
    }
    return other;
}

/*swap:
    return is void
    swap( pointer to 1, pointer to 2 )
    requires one type
    creates extra variable of same type
    stores val 1 into extra variable, val 1 becomes val 2, val 2 becomes extra
*/
template <typename T>
void my_swap(T& a, T& b) {
    T tmp = a;
    a = b;
    b = tmp;
}

/*reverse: (requires my_swap above)
    return is void
    reverse( pointer to first, pointer to end )
    both pointers are of the same type
    loops till first my_equal end and first equals --end
    swaps first and end, then increments first
*/
template <typename I>
void my_reverse(I start, I end) {
    bool sign = start < end;
    while ((start < --end) == sign)
        my_swap(*start++, *end);
}

/* Now I oughtta define my own type that can be stuffed into all these things
   and stick it through (perhaps just a dynamic array or something)*/

int main(int argc, char const *argv[])
{
    std::vector<int> a = {1, 2, 3, 4};
    std::vector<int> b(4, 0); // "my_fill" constructor
    int c = my_count(b.begin(), b.end(), 0);
    assert(c == 4);
    bool d = my_equal(a.begin(), a.end(), b.begin());
    assert (!d);
    my_fill(a.begin(), a.end(), 0);
    assert(my_count(a.begin(), a.end(), 0) == 4);
    a = {1, 2, 3, 4};
    std::vector<int>::iterator asdf = my_find(a.begin(), a.end(), 3);
    assert(asdf == a.begin() + 2);
    std::vector<int>::iterator new_end = my_remove(a.begin(), a.end(), 2);
    assert(new_end - a.begin() == 3);
    my_swap(a[0], a[1]);
    assert(a[0] == 3 && a[1] == 1); // bc we removed the 2 above
    my_reverse(a.begin(), new_end);
    std::vector<int> for_comp = {4, 1, 3};
    assert(my_equal(a.begin(), new_end, for_comp.begin()));
    return 0;
}
