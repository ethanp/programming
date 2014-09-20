#include <city.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>

#define FOUR_KB 1 << 12

using namespace std;

int main() {
    printf("Something\n");

    char *buffer_to_hash = (char*)malloc(FOUR_KB);
    if (buffer_to_hash == NULL)
    {
        printf("Unable to allocate space for buffer_to_hash.\n");
        return -1;
    }

    ifstream urandom_file("/dev/urandom");
    if (!urandom_file.is_open())
    {
        printf("Unable to open urandom\n");
    }

    urandom_file.read(reinterpret_cast<char*>(buffer_to_hash), FOUR_KB);

    // uint128 CityHash128(const char *s, size_t len)
    char s[] = "asdf";
    uint128 a = CityHash128(buffer_to_hash, FOUR_KB);
    cout << a.first << a.second << endl;

    return 0;
}
