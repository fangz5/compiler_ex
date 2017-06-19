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

				scan();

			} else if ( peek == '*' ) { // 		"/* ... */" type comments.
				char prev = ' ';
				boolean isFirstIter = true;
				do {
					if ( isFirstIter ) 	isFirstIter = false;
					else prev = peek;

					peek = (char)System.in.read();
				} while ( prev != '*' || peek != '/');

				scan();

			} else {
				// Throw "Syntax error"!
			}
		}

		// Get number
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				peek = (char)System.in.read();
			} while ( Character.isDigit(peek) );

			return new Num(v);
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

		// Other characters, such '}' or '+'.
		Token t = new Token(peek);
		peek = ' ';
		return t;
	}
}