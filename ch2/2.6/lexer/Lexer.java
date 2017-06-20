package lexer;

import java.io.*;
import java.util.*;


public class Lexer {
	public int line =1;
	private char peek = ' ';
	private Hashtable words = new Hashtable();

	void reserve(Word w) { words.put(w.lexeme, w); }

	public Lexer() {
		reserve(new Word(Tag.TRUE, "true"));
		reserve(new Word(Tag.FALSE, "false"));
	}

	public Token scan() throws IOException {
		// Remove white spaces.
		for ( ; ; peek = (char)System.in.read() ) {
			if (peek == ' ' || peek == '\t') continue;
			else if (peek == '\n') line = line + 1;
			else break;
		}

		// Remove comments.
		if ( peek == '/' ) {
			peek == (char)System.in.read();
			
			if ( peek == '/' ) { 		// 		"//" type comments.
				do {
					peek = (char)System.in.read();
				} while ( peek != '\n' );

			} else if ( peek == '*' ) { // 		"/* ... */" type comments.
				char prev = ' ';
				while ( prev != '*' || peek != '/') {
					prev = peek;
					peek = (char)System.in.read();
				}

			} else {
				// Throw "Syntax error"!
			}
		}

		// Get number
		if (Character.isDigit(peek)) {
			float v = 0;
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				peek = (char)System.in.read();
			} while ( Character.isDigit(peek) );

			if ( peek == '.' ) {
				b.append('.');
				while ( Character.isDigit(peek = (char)System.in.read()) ) {
					b.append(peek);
				}
			}

			return new Num(new Float(b.toString));
		}
		if ( peek == '.' ) {	// .1234 type floats.
			float v = 0;
			StringBuffer b = new StringBuffer();
			while ( Character.isDigit(peek = (char)System.in.read()) ) {
				b.append(peek);
			}

			return new Num(new Float(b.toString));
		}

		// Get word.
		if ( Character.isLetter(peek) ) {
			StringBuffer b = new StringBuffer();

			do {
				b.append(peek);
				peek = (char)System.in.read();
			} while ( Character.isLetterOrDigit(peek) );

			String s = b.toString();
			Word w = (Word)words.get(s);
			if ( w != null ) return w;
			w = new Word(Tag.ID, s);
			words.put(s, w);
			return w;
		}

		// Get relational operation >, >=, ==, !=, <, <=
		if ( "><!=".indexOf(peek) != -1 ) {
			StringBuffer b = new StringBuffer();
			b.append(peek);

			peek = (char)System.in.read();
			if ( peek == '=' ) {
				b.append(peek);
				peek = ' ';
			}

			// '=' is not supported by the grammar yet and should be excluded.

			return new Relation(b.toString);
		}

		// Other characters, such '}' or '+'.
		Token t = new Token(peek);
		peek = ' ';
		return t;
	}
}