package algorithms.dataStructures.difficult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 4/29/15
 *
 * TODO not done. I sort of get the feeling that there's an easier way to go about this...
 */
public class RoyABTree {
    int[] nodes;

    Map<Bag, TableItem[]> table;

    public RoyABTree(int[] n) {
        nodes = n;
        table = new HashMap<>();
        List<Integer> fullList = new ArrayList<>();
        for (int i : n) fullList.add(i);
        Bag all = new Bag(fullList);
        fillTable(all);
    }

    private void fillTable(Bag list) {
        for (int rootIdx = 0; rootIdx < list.size(); rootIdx++) {
            int root = list.get(rootIdx);
            Bag nonRoot = list.allBut(rootIdx);
            getTableEntries(nonRoot);
        }
    }

    private TableItem[] getTableEntries(Bag bag) {
        if (table.containsKey(bag)) {
            return table.get(bag);
        }
        TableItem[] items = new TableItem[nodes.length];

        // TODO recurse
        if (bag.size() == 1) {

        }

        table.put(bag, items);
        return items;
    }

    class TableItem {
        TableItem(int e, int o) { even = e; odd = o; }
        int even, odd;
    }

    class Bag {
        Bag(List<Integer> ints) { items = ints; }

        List<Integer> items;

        public List<Integer> getItemsCopy() {
            return new ArrayList<>(items);
        }

        public int size() { return items.size(); }

        public int get(int index) { return items.get(index); }

        public Bag allBut(int index) {
            List<Integer> copy = new ArrayList<>();
            copy.addAll(items.subList(0,index));
            copy.addAll(items.subList(index+1, items.size()));
            return new Bag(copy);
        }

        public Map<Integer, Integer> getCounts() {
            Map<Integer, Integer> ctr = new HashMap<>();
            for (Integer i : items) {
                if (!ctr.containsKey(i)) {
                    ctr.put(i, 0);
                }
                ctr.put(i, ctr.get(i)+1);
            }
            return ctr;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Bag)) return false;
            Bag bag = (Bag) o;
            return getCounts().equals(bag.getCounts());
        }

        @Override public int hashCode() {
            return getCounts().hashCode();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numTests = sc.nextInt();
        for (int tst = 0; tst < numTests; tst++) {
            int numNodes = sc.nextInt();
            int[] nodes = new int[numNodes];
            for (int idx = 0; idx < numNodes; idx++)
                nodes[idx] = sc.nextInt();
            new RoyABTree(nodes);
        }
    }
}
