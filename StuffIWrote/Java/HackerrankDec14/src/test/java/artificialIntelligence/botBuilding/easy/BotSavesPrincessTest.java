package artificialIntelligence.botBuilding.easy;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Ethan Petuchowski 1/2/16
 */
public class BotSavesPrincessTest {
    @Test public void testReturnPathToPrincess() throws Exception {
        String[] result = BotSavesPrincess.returnPathToPrincess(new String[] {"---","-m-","p--"});
        assertArrayEquals(new String[]{"DOWN", "LEFT"}, result);
    }
}
