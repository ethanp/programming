int main(int argc, char const *argv[])
{
    // the clobber list is telling GCC to not
    // assume that it knows in the register
  __asm__ ("mov $0, %%rdx\n":::"rdx");
  return 0;
}

