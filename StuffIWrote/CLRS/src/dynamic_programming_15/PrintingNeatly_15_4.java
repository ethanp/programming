package dynamic_programming_15;

import java.util.Arrays;

/**
 * Ethan Petuchowski 4/25/16
 *
 * We wish to create an optimal arrangement of the words of a paragraph in a monospaced font. In
 * particular, we wish to minimize the sum, over all lines except the last, of the cubes of the
 * numbers of extra space characters at the ends of lines.
 *
 * Each line has the same fixed length M. Let the length of word i be L_i. So the number of extra
 * space characters at the end of a line containing words i through j is given by
 *
 * M - (j-i) - \sum_{i≤k≤j}{L_k}
 *
 * It is suggested that we use a Dynamic Programming algorithm to solve this efficiently.
 *
 * So what is the subproblem? Well, FOREACH word we must decide whether or not it would be an
 * optimal location to insert a newline character after. It is similar to the rod-cutting problem,
 * except that we are cutting up a String instead of a rod. As we walk through the text, we get the
 * following RECURRENCE RELATION
 *
 * cost at i = min { cost(place newline after i), cost(don't place newline after i) }
 *
 * However, I'm not sure that the aforementioned relation will end up being useful for anything.
 *
 * Let's say that if the line gets too long if we "don't cut after i", then the cost of not cutting
 * is infinity.
 */
public class PrintingNeatly_15_4 {

    static final int INFINITY = Integer.MAX_VALUE;
    static String[] words;
    static int[] lengths;
    static long[] optimalCosts;
    static int[] newlineAfter;
    static int M;

    public static String neatify(String text, int lineLen) {

        // store desired maximum line length
        M = lineLen;

        // split text into words
        words = text.split(" ");

        // map text to word lengths
        lengths = new int[words.length];
        for (int i = 0; i < words.length; i++) {
            lengths[i] = words[i].length();
            if (lengths[i] > M)
                throw new IllegalArgumentException(
                    "This text has a word that will not fit on one line: "+words[i]);
        }

        // This is our cost table. It has the optimal costs after EACH WORD.
        optimalCosts = new long[words.length];
        Arrays.fill(optimalCosts, -1);

        // This is our recovery table. It tells us how to actually print the input text.
        newlineAfter = new int[words.length];
        Arrays.fill(newlineAfter, -1);

        // run the algorithm
        opt(0);

        /* Use the optimization info to create the actual String to return */
        StringBuilder ret = new StringBuilder();
        for (int i = 0, nextNewline = newlineAfter[0]; i < words.length; i++) {
            ret.append(words[i]);
            if (nextNewline == i) {
                if (i+1 < newlineAfter.length)
                    nextNewline = newlineAfter[i+1];
                ret.append('\n');
            }
            else ret.append(' ');
        }
        ret.setLength(ret.length()-1);
        return ret.toString();
    }

    /* This is quite similar to `ChopAhoyRev`, with two major differences:
     *
     * 1) we don't need to keep state about anything before the current index.
     * 2) we do need to keep track of the index at which we're breaking the line
     *   (because we're going to want to recover that info for printing later).
     */
    public static long opt(int startIdx) {
        assert startIdx <= words.length;

        // no cost for empty text (right?)
        if (startIdx == words.length) return 0;

        // does 'r'emainder fit on one line?
        int r = M;
        for (int i = startIdx; i < words.length && r >= 0; i++)
            r -= lengths[i];

        // no cost if it does
        if (r > 0) return 0;

        // return value if already cached
        if (optimalCosts[startIdx] >= 0) return optimalCosts[startIdx];

        /* Try splitting after as many words as will fit on this line. */

        long minCost = INFINITY;
        int minCostIdx = startIdx;
        int charCount = -1; // first word doesn't actually have a 'space' prefix
        for (int curIdx = startIdx; curIdx < words.length; ++curIdx) {

            // add length of this word plus a 'space' prefix
            charCount += 1+lengths[curIdx];

            // ensure we're still within bounds
            if (charCount > M) break;

            // calculate cost of breaking line after this word
            long curLineCost = (long) Math.pow(M-charCount, 3);

            // suppose we went with that
            long remainder = opt(curIdx+1);
            long totalCostHere = remainder+curLineCost;

            // store the best option for breaking up this line
            if (totalCostHere < minCost) {
                minCost = totalCostHere;
                minCostIdx = curIdx;
            }
        }

        /* store and return relevant info */
        newlineAfter[startIdx] = minCostIdx;
        optimalCosts[startIdx] = minCost;
        return minCost;
    }

    public static void main(String[] args) {
        test0();
        test1();
        test2();
        test3();
        test4();
        trial();
    }

    static void shouldBe(String text, int lineLength, String shouldBe) {
        String neatified = neatify(text, lineLength);
        System.out.println(neatified);
        assert neatified.equals(shouldBe);
        System.out.println("PASSED");
    }

    static void test0() { shouldBe("a b", 40, "a b"); }

    static void test1() { shouldBe("a b c d e", 2, "a\nb\nc\nd\ne"); }

    static void test2() { shouldBe("c d e", 3, "c d\ne"); }

    static void test3() { shouldBe("b c d e", 3, "b c\nd e"); }

    static void test4() { shouldBe("a b c d e", 3, "a b\nc d\ne"); }

    static void trial() {
        String text = "TensorFlow [1] is an interface for expressing machine learning "+
            "algorithms, and an implementation for executing such algorithms. A computation "+
            "expressed using TensorFlow can be executed with little or no change on a wide "+
            "variety of heterogeneous systems, ranging from mobile devices such as phones and "+
            "tablets up to large-scale distributed systems of hundreds of machines and thousands "+
            "of computational devices such as GPU cards. The system is flexible and can be used "+
            "to express a wide variety of algorithms, including training and inference "+
            "algorithms for deep neural network models, and it has been used for conducting "+
            "research and for deploying machine learning systems into production across more "+
            "than a dozen areas of computer science and other fields, including speech "+
            "recognition, computer vision, robotics, information retrieval, natural language "+
            "processing, geographic information extraction, and computational drug discovery. "+
            "This paper describes the TensorFlow interface and an implementation of that "+
            "interface that we have built at Google. The TensorFlow API and a reference "+
            "implementation were released as an open-source package under the Apache 2.0 license "+
            "in November, 2015 and are available at www.tensorflow.org.";
        String neatified = neatify(text, 77);
        System.out.println(neatified);
    }
}
