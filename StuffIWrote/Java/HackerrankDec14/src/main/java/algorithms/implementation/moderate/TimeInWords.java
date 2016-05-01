package algorithms.implementation.moderate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ethan Petuchowski 4/24/16
 */
public class TimeInWords {
    static final Map<Integer, String> numberNames = new HashMap<>();

    static {
        numberNames.put(0, "zero");
        numberNames.put(1, "one");
        numberNames.put(2, "two");
        numberNames.put(3, "three");
        numberNames.put(4, "four");
        numberNames.put(5, "five");
        numberNames.put(6, "six");
        numberNames.put(7, "seven");
        numberNames.put(8, "eight");
        numberNames.put(9, "nine");
        numberNames.put(10, "ten");
        numberNames.put(11, "eleven");
        numberNames.put(12, "twelve");
        numberNames.put(13, "thirteen");
        numberNames.put(14, "fourteen");
        numberNames.put(15, "fifteen");
        numberNames.put(16, "sixteen");
        numberNames.put(17, "seventeen");
        numberNames.put(18, "eighteen");
        numberNames.put(19, "nineteen");
        numberNames.put(20, "twenty");
    }

    public static void main(String[] args) {
        System.out.println(timeInWords(1, 0));
        System.out.println(timeInWords(2, 1));
        System.out.println(timeInWords(3, 10));
        System.out.println(timeInWords(12, 30));
        System.out.println(timeInWords(11, 40));
        System.out.println(timeInWords(10, 45));
        System.out.println(timeInWords(9, 47));
        System.out.println(timeInWords(8, 28));
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();
        int m = in.nextInt();
        System.out.println(timeInWords(h, m));
    }

    static String timeInWords(int hour, int minute) {
        String hourName = numberNames.get(hour);
        if (minute == 0) return hourName+" o' clock";
        if (minute == 30) return "half past "+hourName;
        if (minute < 30) return numberInWords(minute)+" past "+hourName;
        int nextHour = hour+1;
        if (nextHour == 13) nextHour = 1;
        return numberInWords(60-minute)+" to "+numberNames.get(nextHour);
    }

    static String numberInWords(int num) {
        if (num >= 30) throw new IllegalArgumentException(String.valueOf(num));
        if (num == 15) return "quarter";
        if (num == 1) return "one minute";
        if (num <= 20) return numberNames.get(num)+" minutes";
        return "twenty "+numberNames.get(num-20)+" minutes";
    }
}
