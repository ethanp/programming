package chp2.lexer;

import java.io.IOException;
import java.text.ParseException;
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
    char nextChar() throws IOException {
        peek = (char) System.in.read();
        return peek;
    }
    void reserve(Word w) {
        words.put(w.lexeme, w);
    }
    public Lexer() {
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }
    public Token scan() throws IOException, ParseException {
        skipWhitespace();
        if (Character.isDigit(peek))
            return handleNumber();
        if (Character.isLetter(peek))
            return handleWord();
        if (peek == '/') {
            nextChar();
            if (peek == '/') return handleLineComment();
            else if (peek == '*') return handleBlockComment();
            else throw new ParseException("expected / or *", line); // simplified
        }
        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    private Token handleBlockComment() throws IOException {
        nextChar();
        StringBuilder b = new StringBuilder();
        for (;;) {
            if (nextChar() == '*') {
                if (nextChar() == '/') {
                    return new Comment(b.toString());
                }
                else {
                    if (peek == '\n') line++;
                    b.append('*').append(peek);
                }
            }
            else {
                if (peek == '\n') line++;
                b.append(peek);
            }
        }
    }

    private Token handleLineComment() throws IOException {
        nextChar();
        StringBuilder b = new StringBuilder();
        while (peek != '\n') {
            b.append(peek);
            nextChar();
        }
        line++;
        return new Comment(b.toString());
    }

    private void skipWhitespace() throws IOException {
        for (;; nextChar()) {
            if (peek == ' ' || peek == '\t') {}
            else if (peek == '\n') line++;
            else break;
        }
    }

    private Token handleWord() throws IOException {
        StringBuilder b = new StringBuilder();
        do {
            b.append(peek);
            nextChar();
        } while (Character.isLetterOrDigit(peek));
        String s = b.toString();
        Word w = words.get(s);
        if (w != null) return w;
        w = new Word(Tag.ID, s);
        words.put(s, w);
        return w;
    }

    private Token handleNumber() throws IOException {
        int v = 0;
        do {
            v = 10*v+Character.digit(peek, 10);
            nextChar();
        } while (Character.isDigit(peek));
        return new Num(v);
    }
}

class TryIt {
    public static void main(String[] args) throws IOException, ParseException {
        for (int i = 0; i < 5; i++)
            System.out.println(
                new Lexer().scan());
    }
}
