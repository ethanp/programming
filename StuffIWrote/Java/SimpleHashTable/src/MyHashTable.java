import java.util.LinkedList;
import java.util.List;

/**
 * Ethan Petuchowski 12/10/14
 */
public class MyHashTable<T> {
    static int SIZE = 50;
    int size = 0;
    List<T> underlyingArray[] = new LinkedList[SIZE];

    private int idx(T obj) {
        return Math.abs(obj.hashCode()%SIZE);
    }
    public void insert(T obj) {
        int idx = idx(obj);
        if (underlyingArray[idx] == null) {
            System.out.println("initializing array elem");
            underlyingArray[idx] = new LinkedList<>();
        }
        for (T elem : underlyingArray[idx]) {
            if (obj.equals(elem)) {
                System.out.println(obj+" already exists in hash table");
                return;
            }
        }
        underlyingArray[idx].add(obj);
        size++;
    }

    public boolean contains(T obj) {
        int idx = idx(obj);
        if (underlyingArray[idx] == null) {
            return false;
        }
        for (T item : underlyingArray[idx]) {
            if (item.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(T obj) {
        int idx = idx(obj);
        if (underlyingArray[idx].contains(obj)) {
            underlyingArray[idx].remove(obj);
            size--;
            return true;
        }
        return false;
    }
}
