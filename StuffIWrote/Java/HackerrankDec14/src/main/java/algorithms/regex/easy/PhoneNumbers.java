package algorithms.regex.easy;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ethan Petuchowski 4/28/15
 */
public class PhoneNumbers {
    public static final Pattern PATTERN = Pattern.compile("(\\d{1,3})[- ](\\d{1,3})[- ](\\d{4,10})");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < N; i++) {
            String s = sc.nextLine();
            process(s);
        }
    }

    private static void process(String s) {
        Matcher matcher = PATTERN.matcher(s);
        if (matcher.find()) {
            System.out.print("CountryCode="+matcher.group(1)+",");
            System.out.print("LocalAreaCode="+matcher.group(2)+",");
            System.out.println("Number="+matcher.group(3));
        }
    }
}
