#include <iostream>

// ------ util ------ //
void just_print(char const * const msg) {
  std::cout << msg << std::endl;
}

template <typename I>
void print_arr(I a, int size) {
  while (size--)
    std::cout << *a++ << ' ';
  std::cout << std::endl;
}

// ------ useful ------ //
template <typename I, typename II>
II array_copy(I a, I e, II b) {
  while (a != e)
    *b++ = *a++;
  return b;
}

// ------ main ------ //
int main(int argc, char const *arv[]) {
  int a[] = {1, 2, 3, 4};
  int b[] = {0, 0, 0, 0};
  int *c = array_copy(a, a+2, b);
  int arr_len = sizeof(a)/sizeof(int);
  just_print("A:");
  print_arr(a, arr_len);
  just_print("B:");
  print_arr(b, arr_len);
  return 0;
}
