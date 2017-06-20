package lexer;

public class Relation extends Token {
	public final String op;

	public Relation(String s) {
		super(Tag.RELATION);
		value = s;
	}
}