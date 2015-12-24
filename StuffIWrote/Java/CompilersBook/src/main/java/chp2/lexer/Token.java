package chp2.lexer;

/**
 * Ethan Petuchowski 12/24/15
 */
public class Token {
    public final int tag;
    public Token(int tag) {
        this.tag = tag;
    }
    @Override public String toString() {
        return "Token{"+
            "tag="+tag+
            '}';
    }
}
