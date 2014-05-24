package SimplerInheritance;

/**
 * Ethan Petuchowski 5/23/14
 */
public class Base<T>
{
    T elem;
    Base<T> next;

    Base(T elem) {
        this.elem = elem;
    }
}
