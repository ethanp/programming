import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class RBLLTest {
    RBLL rbll;

    @Before
    public void setUp() throws Exception {
        rbll = new RBLL();
    }

    @Test
    public void testSequentialSeparateIntervals() throws Exception {
        rbll.addInterval(2, 5);
        assertEquals(1, rbll.size());
        rbll.addInterval(8, 12);
        rbll.addInterval(18, 22);
        rbll.addInterval(28, 32);
        assertEquals(4, rbll.size());
    }

    @Test
    public void testBackwardsSeparateIntervals() throws Exception {
        rbll.addInterval(2, 5);
        assertEquals(1, rbll.size());
        rbll.addInterval(0, 1);
        assertEquals(2, rbll.size());
        rbll.addInterval(-5, -2);
        rbll.addInterval(-15, -12);
        assertEquals(4, rbll.size());
    }

    @Test
    public void testInterspersedSeparateIntervals() throws Exception {
        rbll.addInterval(2, 5);
        rbll.addInterval(6, 8);
        rbll.addInterval(23, 43);
        rbll.addInterval(11, 22);
        rbll.addInterval(-4, -3);
        rbll.addInterval(0, 1);
        assertEquals(6, rbll.size());
    }

    @Test
    public void testAddMergeForwardIntervals() throws Exception {
        rbll.addInterval(0, 5);
        rbll.addInterval(20, 25);
        rbll.addInterval(15, 25);
        assertEquals(2, rbll.size());
        rbll.addInterval(20, 30);
        assertEquals(2, rbll.size());
    }

    @Test
    public void testAddMergeBackwardsIntervals() throws Exception {
        rbll.addInterval(0, 10);
        rbll.addInterval(8, 9);
        assertEquals(1, rbll.size());
        rbll.addInterval(8, 19);
        assertEquals(1, rbll.size());

    }

    @Test
    public void testEquals() throws Exception {
        RBLL rbll2 = new RBLL();
        RBLL rbll3 = new RBLL();
        rbll.addInterval(0, 6);
        rbll2.addInterval(0, 6);
        assertEquals(rbll, rbll2);
        assertThat(rbll, not(equalTo(rbll3)));
        rbll.addInterval(10, 16);
        rbll2.addInterval(10, 16);
        assertEquals(rbll, rbll2);
        assertThat(rbll, not(equalTo(rbll3)));
        rbll.addInterval(4, 16);
        rbll2.addInterval(4, 16);
        assertEquals(rbll, rbll2);
        assertThat(rbll, not(equalTo(rbll3)));
    }

    @Test
    public void testContainsInt() throws Exception {
        assertFalse(rbll.containsInt(4));
        rbll.addInterval(0, 2);
        assertFalse(rbll.containsInt(4));
        assertTrue(rbll.containsInt(0));
        assertTrue(rbll.containsInt(1));
        assertTrue(rbll.containsInt(2));
    }

    @Test
    public void testGetIntervalContaining() throws Exception {
        assertNull(rbll.getIntervalContaining(3));
        rbll.addInterval(0, 2);
        assertNull(rbll.getIntervalContaining(3));
        assertEquals(new RBLL.LibBasedNode(0, 2), rbll.getIntervalContaining(1));
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, rbll.size());
        rbll.addInterval(0, 4);
        assertEquals(1, rbll.size());
    }

    @Test
    public void testClear() throws Exception {
        rbll.clear();
        assertEquals(0, rbll.size());
        rbll.addInterval(0, 4);
        assertEquals(1, rbll.size());
        rbll.clear();
        assertEquals(0, rbll.size());
    }
}
