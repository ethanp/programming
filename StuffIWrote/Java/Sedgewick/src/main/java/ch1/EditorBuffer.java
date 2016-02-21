package ch1;

import java.util.Stack;

/**
 * Ethan Petuchowski 2/20/16
 *
 * 1.3.44 Develop a data type for a buffer in a text editor. (Hint use two stacks.)
 *
 * I took the further 'hint' of looking up the basics of this thing in Wikipedia. So my
 * implementation is "based-on" a gap-buffer, but is slightly simpler.
 */
public class EditorBuffer {

    /* uses a `Vector` and implements "Stack-like" API on top */
    private final Stack<Character> leftStack = new Stack<>();
    private final Stack<Character> rightStack = new Stack<>();

    /** create an empty buffer */
    public EditorBuffer() {}

    public static void main(String[] args) {
        EditorBuffer eb = new EditorBuffer();
        eb.insert('h');
        eb.insert('e');
        eb.insert('w');
        eb.insert('r');
        eb.insert('l');
        eb.insert('d');
        eb.left(3);
        eb.insert('o');
        eb.left(2);
        eb.insert('l');
        eb.insert('l');
        eb.insert('o');
        eb.insert(' ');
        eb.right(5);
        eb.insert('!');
        System.out.println(eb.toString());
        eb.left(6);
        eb.delete(); // delete space
        System.out.println(eb.toString());
    }

    /** insert c at the cursor position */
    public void insert(char c) {
        leftStack.push(c);
    }

    /** delete and return the character at the cursor */
    public char delete() {
        if (leftStack.isEmpty())
            return 0;
        return leftStack.pop();
    }

    /** move the cursor k positions to the left */
    public void left(int k) {
        for (int i = 0; i < k; i++) {
            if (!leftStack.isEmpty()) {
                rightStack.push(leftStack.pop());
            }
        }
    }

    /** move the cursor k positions to the right , */
    public void right(int k) {
        for (int i = 0; i < k; i++) {
            if (!rightStack.isEmpty()) {
                leftStack.push(rightStack.pop());
            }
        }
    }

    /** number of characters in the buffer */
    public int size() {
        return leftStack.size()+rightStack.size();
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        Stack<Character> reverser = new Stack<>();
        leftStack.forEach(sb::append);
        rightStack.forEach(reverser::add);
        while (!reverser.isEmpty()) {
            sb.append(reverser.pop());
        }
        return sb.toString();
    }
}
