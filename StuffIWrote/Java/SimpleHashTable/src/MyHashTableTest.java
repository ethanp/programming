import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MyHashTableTest {
    MyHashTable<Integer> hsh;

    @Before
    public void setup() throws Exception {
        hsh = new MyHashTable<>();
    }

    @Test
    public void myTest1() throws Exception {
        assertFalse(hsh.contains(3));
        hsh.insert(3);
        assertTrue(hsh.contains(3));
        hsh.insert(3);
        assertTrue(hsh.contains(3));
        hsh.insert(5);
        assertFalse(hsh.contains(4));
        assertTrue(hsh.contains(5));
        assertTrue(hsh.contains(3));
    }

    @Test
    public void myTest2() throws Exception {
        Random r = new Random();
        final int REPS = 100000;
        int inserts[] = new int[REPS];
        for (int i = 0; i < REPS; i++) {
            int j = r.nextInt();
            inserts[i] = j;
            hsh.insert(j);
            assertTrue(hsh.contains(j));
        }
        for (int i = 0; i < REPS; i++) {
            hsh.remove(inserts[i]);
        }
        assertEquals(0, hsh.size);
    }
}
