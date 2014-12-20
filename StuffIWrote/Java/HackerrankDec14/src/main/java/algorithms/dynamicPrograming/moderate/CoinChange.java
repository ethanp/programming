package algorithms.dynamicPrograming.moderate;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Ethan Petuchowski 12/19/14
 * This is a 4-line solution that took 1.5 hours.
 */
public class CoinChange {

    static int[][] table;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] coinsStrArr = in.nextLine().replaceAll(" ","").split(",");
        int coins = coinsStrArr.length;
        int[] coinValues = new int[coins];
        for (int i = 0; i < coins; i++)
            coinValues[i] = Integer.parseInt(coinsStrArr[i]);
        Arrays.sort(coinValues);
        int totalAmount = in.nextInt();

        /** this seems to be one of those "build up from a table" problems */

        /* set up the table, 1st dim is num (sorted) coins allowed, 2nd is amt */
        table = new int[coins+1][totalAmount+1];

        /* base cases on both dims are zeros */
        for (int i = 0; i < table.length; i++)
            table[i][0] = 0;
        for (int i = 0; i < table[0].length; i++)
            table[0][i] = 0;

        for (int coin = 1; coin <= coins; coin++) {
            int thisCoinVal = coinValues[coin-1];
            for (int amount = 1; amount <= totalAmount; amount++) {
                int addIt = amount == thisCoinVal ? 1 : 0;
                int above = tableGetter(coin-1, amount);
                int toTheLeft = tableGetter(coin, amount-thisCoinVal);
                table[coin][amount] = addIt + above + toTheLeft;
            }
        }
        System.out.println(table[coins][totalAmount]);
    }

    static int tableGetter(int coin, int amt) {
        if (coin < 1 || amt < 1) return 0;
        else return table[coin][amt];
    }
}
