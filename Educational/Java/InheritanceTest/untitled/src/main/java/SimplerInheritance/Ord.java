package SimplerInheritance;

/**
 * Ethan Petuchowski 5/23/14
 */
class Ord<U extends Comparable<U>> extends Base<U> implements Comparable<Ord<U>>
{
    Ord(U e) {
        super(e);
    }

    @Override
    public int compareTo(Ord<U> o) {
        return this.elem.compareTo(o.elem);
    }

    public static void main(String[] args) {
        Ord<Integer> a = new Ord<Integer>(3);
        Ord<Integer> b = new Ord<Integer>(4);
        System.out.println(a.compareTo(b) < 0);
        a.next = b;
//        System.out.println(a.compareTo(a.next));  // NO CAN DO
        System.out.println(a.elem.compareTo(a.next.elem));
    }
}
