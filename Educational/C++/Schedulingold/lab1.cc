#include <city.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <time.h>       /* time_t, struct tm, difftime, time, mktime */

#define FOUR_KB 1 << 12

using namespace std;

int main() {
    printf("Something\n");

    char *buffer_to_hash = (char*)malloc(FOUR_KB);
    if (buffer_to_hash == NULL)
    {
        printf("Unable to allocate space for buffer-to-hash.\n");
        return -1;
    }

    ifstream urandom_file("/dev/urandom");
    if (!urandom_file.is_open())
    {
        printf("Unable to open urandom\n");
        return -1;
    }

    urandom_file.read(reinterpret_cast<char*>(buffer_to_hash), FOUR_KB);
    uint128 a;

    time_t start, stop;
    double seconds;
    time(&start);

    int REPEATS = 550000; // 550,000
    for (int i = 0; i < REPEATS; i++)
    {
        // uint128 CityHash128(const char *s, size_t len)
        a = CityHash128(buffer_to_hash, FOUR_KB);
    }

    time(&stop);
    seconds = difftime(stop, start);

    printf("%.f seconds elapsed doing %d hashes\n", seconds, REPEATS);

    cout << a.first << a.second << endl;
    return 0;
}
