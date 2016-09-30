package uvaoj.primary_arithmetic;

import java.util.Scanner;

/**
 * 9/26/16 8:08 PM
 *
 * count the number of carry operations for each of a set of addition
 * problems
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            long a = sc.nextLong();
            long b = sc.nextLong();
            if (a == 0 && b == 0) {
                break;
            }
            int carry = 0;
            int carryCount = 0;
            while (a > 0 && b > 0 || (a > 0 || b > 0) && carry > 0) {
                int l = (int) (a%10);
                int r = (int) (b%10);
                a /= 10;
                b /= 10;
                carry = (l + r + carry) / 10;
                carryCount += carry;
            }
            if (carryCount == 0) {
                System.out.println("No carry operation.");
            } else if (carryCount == 1) {
                System.out.println("1 carry operation.");
            } else {
                System.out.println(carryCount + " carry operations.");
            }
        }
    }
}
