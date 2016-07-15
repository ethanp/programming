package ch4;

/**
 * 7/10/16 10:53 PM
 */
class Forest {
    final boolean[] hasBeenSeen;
    private int nextIdx = 0;

    public Forest(int numNodes) {
        hasBeenSeen = new boolean[numNodes];
    }

    public boolean mark(int idx) {
        if (hasBeenSeen[idx]) return false;
        hasBeenSeen[idx] = true;
        if (idx == nextIdx) {
            while (++nextIdx < hasBeenSeen.length && hasBeenSeen[nextIdx]) ;
        }
        return true;
    }

    public boolean hasNext() {
        return nextIdx < hasBeenSeen.length;
    }
}
