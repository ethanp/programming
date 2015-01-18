import generic.GenericChild;
import generic.GenericParent;
import nongeneric.NongenericChild;
import nongeneric.NongenericParent;
import toret.Child;
import toret.Parent;

/**
 * Ethan Petuchowski 1/15/15
 */
public class Run {
    public static void main(String[] args) {
        Parent parent = new Parent();
        Child child = new Child();
        NongenericParent nongenericParent = new NongenericParent();
        NongenericChild nongenericChild = new NongenericChild();
        GenericParent genericParent = new GenericParent();
        GenericChild genericChild = new GenericChild();

        System.out.println(parent);
        System.out.println(child);

        System.out.println(nongenericParent);
        System.out.println(nongenericChild);

        System.out.println(genericParent);
        System.out.println(genericChild);

        System.out.println("\nNow Incrementing...\n");

        genericParent.incr();
        genericChild.incr();

        System.out.println(genericParent);
        System.out.println(genericChild);
    }
}
