package chp2.lexer;

/**
 * Ethan Petuchowski 12/24/15
 */
public class Num extends Token {
    public final int value;
    public Num(int value) {
        super(Tag.NUM);
        this.value = value;
    }

    @Override public String toString() {
        return "Num{"+
            "value="+value+
            '}';
    }
}
