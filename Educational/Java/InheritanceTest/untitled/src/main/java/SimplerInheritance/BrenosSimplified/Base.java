package SimplerInheritance.BrenosSimplified;

/**
 * Ethan Petuchowski 5/23/14
 */
public class Base<T, E extends Base>
{
    T elem;
    E next;

    Base(T elem) {
        this.elem = elem;
    }
}
