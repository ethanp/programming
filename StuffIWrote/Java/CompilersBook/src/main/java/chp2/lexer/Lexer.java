package chp2.lexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Ethan Petuchowski 12/24/15
 *
 * This is code from "Compilers" (Dragon book) Chapter 2
 */
public class Lexer {
    public int line = 1;
    private char peek = ' ';
    private Map<String, Word> words = new HashMap<String, Word>();
    void reserve(Word w) {
        words.put(w.lexeme, w);
    }
    public Lexer() {
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }
    public Token scan() throws IOException {
        for (;; peek = (char) System.in.read()) {
            if (peek == ' ' || peek == '\t') {}
            else if (peek == '\n') line++;
            else break;
        }
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10*v+Character.digit(peek, 10);
                peek = (char) System.in.read();
            } while (Character.isDigit(peek));
            return new Num(v);
        }
        if (Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                peek = (char) System.in.read();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = words.get(s);
            if (w != null) return w;
            w = new Word(Tag.ID, s);
            words.put(s, w);
            return w;
        }
        Token t = new Token(peek);
        peek = ' ';
        return t;
    }
}

class TryIt {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 5; i++)
            System.out.println(
                new Lexer().scan());
    }
}
