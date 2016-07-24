package ch5;

/**
 * 7/24/16 1:30 PM
 *
 * I was not able to quite figure out how the last line in the table-building step works.
 *
 * This is the "real-time" version of the algorithm because it has a location for every char from
 * every state, is good for input streams of undetermined length, but significantly slower than
 * "Boyer-Moore" when backing up is easy.
 */
public class KMP {
    final static int ALPHABET_SIZE = 1 << 8;
    final String pattern;
    final int[][] dfa;

    public KMP(String pattern) {
        this.pattern = pattern;
        this.dfa = new int[ALPHABET_SIZE][pattern.length()];
        buildDFA();
    }

    public static void main(String[] args) {
        int idx = new KMP("abac").findIn("abadabaegabacdeg");
        int expected = 9;
        System.out.println(idx);
        System.out.println(idx == expected);
    }

    /**
     * "We only need to know how the DFA runs for j-1 characters when we are building the jth state"
     * -- Sedgewick. We iterate in column-major order, and for each slot, fill in the place that we
     * would have to jump back to if we had a mismatch.
     *
     * Terminology: A "proper" (pre|suf)fix means it is _strictly_ shorter than the original
     * string.
     *
     * 'Each value in the table corresponds to the length of the longest proper prefix of the
     * portion of the pattern we are considering that also matches a proper suffix of this portion
     * of the pattern' -- (paraphrasing) Jake. E.g. "ababa" has a proper prefix "aba", which is also
     * a proper suffix. Since this is the longest such substring of "ababa", we assign "3" (the
     * length of the substring) to the associated cell in the table.
     *
     * Jake: http://jakeboxer.com/blog/2009/12/13/the-knuth-morris-pratt-algorithm-in-my-own-words
     */
    private void buildDFA() {
        // If we see the first char when nothing has been matched so far, move to the 2nd char.
        // For any other char in this col, we leave it as a transition back to the start state.
        dfa[pattern.charAt(0)][0] = 1;

        // Fill in table for the states where we've already matched at least one char
        for (int backupLoc = 0, matchState = 1; matchState < pattern.length(); matchState++) {
            // Start by copying the current backupLoc's column into this column.
            // At first this will mean copy the left column into the 2nd-left column.
            for (int charIdx = 0; charIdx < ALPHABET_SIZE; charIdx++)
                dfa[charIdx][matchState] = dfa[charIdx][backupLoc];
            // As an exception, if we have the correct next char, then we move to the next state.
            dfa[pattern.charAt(matchState)][matchState] = matchState + 1;
            // The next column to copy from would be the state we would go to if we found the
            // correct char in the current state?
            backupLoc = dfa[pattern.charAt(matchState)][backupLoc];
        }
    }

    public int findIn(String text) {
        int txtIdx = 0, matchState = 0;
        // Traverse the DFA using the input text.
        for (; txtIdx < text.length() && matchState < pattern.length(); txtIdx++)
            // Jump to the next match state using the dfa table.
            matchState = dfa[text.charAt(txtIdx)][matchState];
        // If we've matched till the end, return the location the match started at.
        if (matchState == pattern.length()) return txtIdx - pattern.length();
            // Otw return "not found", i.e. -1.
        else return -1;
    }
}
