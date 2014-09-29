int main(int argc, char *arg[]) {
    char *s = "asdf";
    // printf(s+"qwer\n"); (this crashes)
    for (int i = 0; i < argc; i++) {
        printf("%d: %s\n", i, arg[i]);
    }
}
