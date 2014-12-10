import java.security.InvalidParameterException;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Ethan Petuchowski 12/8/14
 */
public class RBLL {
    NavigableSet<LibBasedNode> tree = new TreeSet<>();
    LibBasedNode first = null;

    // the linked list in implicit via the `prev/next` fields of the nodes

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RBLL)) return false;
        RBLL that = (RBLL) o;

        /* compare tree sizes, then do a linked-list comparison */
        if (first == null && that.first == null) return true;
        if (first == null || that.first == null) return false;
        if (size() != that.size()) return false;
        LibBasedNode a = first,
                     b = that.first;
        while (a != null) {
            assert b != null;
            if (!a.equals(b))
                return false;
            a = a.next;
            b = b.next;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return first != null ? first.hashCode() : 0;
    }

    static class LibBasedNode implements Comparable {
        int start,
            end;
        LibBasedNode prev = null,
                     next = null;

        public LibBasedNode(int s, int e) {
            if (start > end) {
                throw new InvalidParameterException("start can't exceed end");
            }
            start = s;
            end = e;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LibBasedNode)) return false;
            LibBasedNode that = (LibBasedNode) o;
            if (end != that.end) return false;
            if (start != that.start) return false;
            return true;
        }
        @Override
        public int hashCode() {
            int result = start;
            result = 31*result+end;
            return result;
        }
        /**
         * @param   o the object to be compared.
         * @return  a negative integer, zero, or a positive integer as this object
         *          is less than, equal to, or greater than the specified object.
         *
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException if the specified object's type prevents it
         *         from being compared to this object.
         */
        @Override
        public int compareTo(Object o) {
            if (o == null) {
                throw new NullPointerException("can't compare node with null");
            }
            if (!(o instanceof  LibBasedNode)) {
                throw new ClassCastException("can't compare node with non-node");
            }
            LibBasedNode that = (LibBasedNode) o;
            return ((Integer)this.start).compareTo(that.start);
        }
        @Override
        public String toString() {
            return "LibBasedNode{"+"start="+start+", end="+end+'}';
        }
    }

    public boolean containsInt(int elem) {
        return getIntervalContaining(elem) != null;
    }

    public LibBasedNode getIntervalContaining(int elem) {
        LibBasedNode forComparison = new LibBasedNode(elem, elem);
        LibBasedNode firstUnder = tree.floor(forComparison);
        if (firstUnder == null) {
            return null;
        }
        LibBasedNode curr = firstUnder;
        while (curr.end < elem) {
            if (curr.next == null) {
                return null;
            }
            curr = curr.next;
        }
        return curr;
    }

    @Override
    public String toString() {
        return tree.size()+" nodes: LibBasedRBLL{"+tree+'}';
    }

    public int size() { return tree.size(); }

    private void addAsFirst(LibBasedNode node) {

        /* set as first node in list */
        first.prev = node;
        node.next = first;
        first = node;

        /* merge with following nodes */
        LibBasedNode curr = node.next;
        while (curr != null && curr.start < node.end) {
            /* overlaps, so merge and remove the node */
            node.end = Math.max(curr.start, node.end);
            LibBasedNode old = curr;
            curr = curr.next;
            tree.remove(old);
        }
        node.next = curr;

        /* add to tree */
        tree.add(node);
    }

    public void clear() {
        tree.clear();
        first = null;
    }

    public void addInterval(int start, int end) {
        LibBasedNode toAdd = new LibBasedNode(start, end);

        if (tree.size() == 0) {
            first = toAdd;
            tree.add(toAdd);
            return;
        }

        /* find the preceding node */
        LibBasedNode floor = tree.floor(toAdd);

        if (floor == null) {
            /* no one precedes */
            addAsFirst(toAdd);
            return;
        }

        else {
            /* there was a node lte, merge any overlaps */
            LibBasedNode curr = floor;

            if (floor.end > toAdd.start) {
                /* overlaps preceding elem */
                floor.end = Math.max(floor.end, toAdd.end);

                /* merge through list */
                while (curr.next != null && curr.end > curr.next.start) {
                    curr.end = Math.max(curr.end, curr.next.end);
                    LibBasedNode toRem = curr.next;
                    curr.next = toRem.next;
                    tree.remove(toRem);
                }
                return; // we're done now.
            }

            /* insert new node into list & tree */
            toAdd.next = floor.next;
            floor.next = toAdd;
            tree.add(toAdd);
            curr = toAdd;

            /* roll through and merge it in */
            while (curr.next != null && curr.end > curr.next.start) {
                curr.end = Math.max(curr.end, curr.next.end);
                LibBasedNode toRem = curr.next;
                curr.next = toRem.next;
                tree.remove(toRem);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("ain't no other main I can staind, it's True");
    }

}
