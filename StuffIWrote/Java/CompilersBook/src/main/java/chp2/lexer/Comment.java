package chp2.lexer;

/**
 * Ethan Petuchowski 12/24/15
 */
public class Comment extends Token {
    public final String text;

    public Comment(String text) {
        super(Tag.COMMENT);
        this.text = text;
    }

    @Override public String toString() {
        return "Comment{"+
            "text='"+text+'\''+
            '}';
    }
}
