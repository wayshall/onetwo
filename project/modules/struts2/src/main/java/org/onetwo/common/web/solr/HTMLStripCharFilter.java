package org.onetwo.common.web.solr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.analysis.BaseCharFilter;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;

@SuppressWarnings({"unchecked", "unused"})
public class HTMLStripCharFilter extends BaseCharFilter {

	private int readAheadLimit = DEFAULT_READ_AHEAD;
	private int safeReadAheadLimit = readAheadLimit - 3;
	private int numWhitespace = 0;
	private int numRead = 0;
	private int numEaten = 0;
	private int numReturned = 0;
	private int lastMark;
	private Set<String> escapedTags;

	// pushback buffer
	private final StringBuilder pushed = new StringBuilder();
	private static final int EOF = -1;
	private static final int MISMATCH = -2;

	private static final int MATCH = -3;
	// temporary buffer
	private final StringBuilder sb = new StringBuilder();
	public static final int DEFAULT_READ_AHEAD = 8192;

	public static void main(String[] args) throws IOException {
		Reader in = new HTMLStripCharFilter(CharReader.get(new InputStreamReader(System.in)));
		int ch;
		while ((ch = in.read()) != -1)
			System.out.print((char) ch);
	}

	public HTMLStripCharFilter(CharStream source) {
		super(source.markSupported() ? source : CharReader.get(new BufferedReader(source)));
	}

	public HTMLStripCharFilter(CharStream source, Set<String> escapedTags) {
		this(source);
		this.escapedTags = escapedTags;
	}

	public HTMLStripCharFilter(CharStream source, Set<String> escapedTags, int readAheadLimit) {
		this(source);
		this.escapedTags = escapedTags;
		this.readAheadLimit = readAheadLimit;
		safeReadAheadLimit = readAheadLimit - 3;
	}

	public int getReadAheadLimit() {
		return readAheadLimit;
	}

	private int next() throws IOException {
		int len = pushed.length();
		if (len > 0) {
			int ch = pushed.charAt(len - 1);
			pushed.setLength(len - 1);
			return ch;
		}
		numRead++;
		int ch = input.read();
		return ch;
	}

	private int nextSkipWS() throws IOException {
		int ch = next();
		while (isSpace(ch))
			ch = next();
		return ch;
	}

	private int peek() throws IOException {
		int len = pushed.length();
		if (len > 0) {
			return pushed.charAt(len - 1);
		}
		int ch = input.read();
		push(ch);
		return ch;
	}

	private void push(int ch) {
		pushed.append((char) ch);
	}

	private boolean isSpace(int ch) {
		switch (ch) {
		case ' ':
		case '\n':
		case '\r':
		case '\t':
			return true;
		default:
			return false;
		}
	}

	private boolean isHex(int ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
	}

	private boolean isAlpha(int ch) {
		return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
	}

	private boolean isDigit(int ch) {
		return ch >= '0' && ch <= '9';
	}

	/***************************************************************************
	 * * From HTML 4.0 [4] NameChar ::= Letter | Digit | '.' | '-' | '_' | ':' |
	 * CombiningChar | Extender [5] Name ::= (Letter | '_' | ':') (NameChar)*
	 * [6] Names ::= Name (#x20 Name)* [7] Nmtoken ::= (NameChar)+ [8] Nmtokens
	 * ::= Nmtoken (#x20 Nmtoken)*
	 **************************************************************************/

	// should I include all id chars allowable by HTML/XML here?
	// including accented chars, ':', etc?
	private boolean isIdChar(int ch) {
		// return Character.isUnicodeIdentifierPart(ch);
		// isUnicodeIdentiferPart doesn't include '-'... shoudl I still
		// use it and add in '-',':',etc?
		return isAlpha(ch) || isDigit(ch) || ch == '.' || ch == '-' || ch == '_' || ch == ':' || Character.isLetter(ch);

	}

	private boolean isFirstIdChar(int ch) {
		return Character.isUnicodeIdentifierStart(ch);
		// return isAlpha(ch) || ch=='_' || Character.isLetter(ch);
	}

	private void saveState() throws IOException {
		lastMark = numRead;
		input.mark(readAheadLimit);
	}

	private void restoreState() throws IOException {
		input.reset();
		pushed.setLength(0);
	}

	private int readNumericEntity() throws IOException {
		// "&#" has already been read at this point
		int eaten = 2;

		// is this decimal, hex, or nothing at all.
		int ch = next();
		int base = 10;
		boolean invalid = false;
		sb.setLength(0);

		if (isDigit(ch)) {
			// decimal character entity
			sb.append((char) ch);
			for (int i = 0; i < 10; i++) {
				ch = next();
				if (isDigit(ch)) {
					sb.append((char) ch);
				} else {
					break;
				}
			}
		} else if (ch == 'x') {
			eaten++;
			// hex character entity
			base = 16;
			sb.setLength(0);
			for (int i = 0; i < 10; i++) {
				ch = next();
				if (isHex(ch)) {
					sb.append((char) ch);
				} else {
					break;
				}
			}
		} else {
			return MISMATCH;
		}

		// In older HTML, an entity may not have always been terminated
		// with a semicolon. We'll also treat EOF or whitespace as terminating
		// the entity.
		try {
			if (ch == ';' || ch == -1) {
				// do not account for the eaten ";" due to the fact that we do
				// output a char
				numWhitespace = sb.length() + eaten;
				return Integer.parseInt(sb.toString(), base);
			}

			// if whitespace terminated the entity, we need to return
			// that whitespace on the next call to read().
			if (isSpace(ch)) {
				push(ch);
				numWhitespace = sb.length() + eaten;
				return Integer.parseInt(sb.toString(), base);
			}
		} catch (NumberFormatException e) {
			return MISMATCH;
		}

		// Not an entity...
		return MISMATCH;
	}

	private int readEntity() throws IOException {
		int ch = next();
		if (ch == '#')
			return readNumericEntity();

		// read an entity reference

		// for an entity reference, require the ';' for safety.
		// otherwise we may try and convert part of some company
		// names to an entity. "Alpha&Beta Corp" for instance.
		//
		// TODO: perhaps I should special case some of the
		// more common ones like &amp to make the ';' optional...

		sb.setLength(0);
		sb.append((char) ch);

		for (int i = 0; i < safeReadAheadLimit; i++) {
			ch = next();
			if (Character.isLetter(ch)) {
				sb.append((char) ch);
			} else {
				break;
			}
		}

		if (ch == ';') {
			String entity = sb.toString();
			Character entityChar = entityTable.get(entity);
			if (entityChar != null) {
				numWhitespace = entity.length() + 1;
				return entityChar.charValue();
			}
		}

		return MISMATCH;
	}

	/***************************************************************************
	 * * valid comments according to HTML specs <!-- Hello --> <!-- Hello -- --
	 * Hello--> <!----> <!------ Hello --> <!> <!------> Hello -->
	 * 
	 * #comments inside of an entity decl: <!ENTITY amp CDATA "&#38;" --
	 * ampersand, U+0026 ISOnum -->
	 * 
	 * Turns out, IE & mozilla don't parse comments correctly. Since this is
	 * meant to be a practical stripper, I'll just try and duplicate what the
	 * browsers do.
	 * 
	 * <!-- (stuff_including_markup)* --> <!FOO (stuff, not including markup) > <!
	 * (stuff, not including markup)* >
	 * 
	 * 
	 **************************************************************************/

	private int readBang(boolean inScript) throws IOException {
		// at this point, "<!" has been read
		int ret = readComment(inScript);
		if (ret == MATCH)
			return MATCH;

		if ((numRead - lastMark) < safeReadAheadLimit || peek() == '>') {

			int ch = next();
			if (ch == '>')
				return MATCH;

			// if it starts with <! and isn't a comment,
			// simply read until ">"
			// since we did readComment already, it may be the case that we are
			// already deep into the read ahead buffer
			// so, we may need to abort sooner
			while ((numRead - lastMark) < safeReadAheadLimit) {
				ch = next();
				if (ch == '>') {
					return MATCH;
				} else if (ch < 0) {
					return MISMATCH;
				}
			}
		}
		return MISMATCH;
	}

	// tries to read comments the way browsers do, not
	// strictly by the standards.
	//
	// GRRRR. it turns out that in the wild, a <script> can have a HTML comment
	// that contains a script that contains a quoted comment.
	// <script><!-- document.write("<!--embedded comment-->") --></script>
	//
	private int readComment(boolean inScript) throws IOException {
		// at this point "<!" has been read
		int ch = next();
		if (ch != '-') {
			// not a comment
			push(ch);
			return MISMATCH;
		}

		ch = next();
		if (ch != '-') {
			// not a comment
			push(ch);
			push('-');
			return MISMATCH;
		}
		/*
		 * two extra calls to next() here, so make sure we don't read past our
		 * mark
		 */
		while ((numRead - lastMark) < safeReadAheadLimit - 3) {
			ch = next();
			if (ch < 0)
				return MISMATCH;
			if (ch == '-') {
				ch = next();
				if (ch < 0)
					return MISMATCH;
				if (ch != '-') {
					push(ch);
					continue;
				}

				ch = next();
				if (ch < 0)
					return MISMATCH;
				if (ch != '>') {
					push(ch);
					push('-');
					continue;
				}

				return MATCH;
			} else if ((ch == '\'' || ch == '"') && inScript) {
				push(ch);
				int ret = readScriptString();
				// if this wasn't a string, there's not much we can do
				// at this point without having a stack of stream states in
				// order to "undo" just the latest.
			} else if (ch == '<') {
				eatSSI();
			}

		}
		return MISMATCH;

	}

	private int readTag() throws IOException {
		// at this point '<' has already been read
		int ch = next();
		if (!isAlpha(ch)) {
			push(ch);
			return MISMATCH;
		}

		sb.setLength(0);
		sb.append((char) ch);
		while ((numRead - lastMark) < safeReadAheadLimit) {

			ch = next();
			if (isIdChar(ch)) {
				sb.append((char) ch);
			} else if (ch == '/') {
				// Hmmm, a tag can close with "/>" as well as "/ >"
				// read end tag '/>' or '/ >', etc
				return nextSkipWS() == '>' ? MATCH : MISMATCH;
			} else {
				break;
			}
		}
		if (escapedTags != null && escapedTags.contains(sb.toString())) {
			// if this is a reservedTag, then keep it
			return MISMATCH;
		}
		// After the tag id, there needs to be either whitespace or
		// '>'
		if (!(ch == '>' || isSpace(ch))) {
			return MISMATCH;
		}

		if (ch != '>') {
			// process attributes
			while ((numRead - lastMark) < safeReadAheadLimit) {
				ch = next();
				if (isSpace(ch)) {
					continue;
				} else if (isFirstIdChar(ch)) {
					push(ch);
					int ret = readAttr2();
					if (ret == MISMATCH)
						return ret;
				} else if (ch == '/') {
					// read end tag '/>' or '/ >', etc
					return nextSkipWS() == '>' ? MATCH : MISMATCH;
				} else if (ch == '>') {
					break;
				} else {
					return MISMATCH;
				}

			}
			if ((numRead - lastMark) >= safeReadAheadLimit) {
				return MISMATCH;// exit out if we exceeded the buffer
			}
		}

		// We only get to this point after we have read the
		// entire tag. Now let's see if it's a special tag.
		String name = sb.toString();
		if (name.equalsIgnoreCase("script") || name.equalsIgnoreCase("style")) {
			// The content of script and style elements is
			// CDATA in HTML 4 but PCDATA in XHTML.

			/*
			 * From HTML4: Although the STYLE and SCRIPT elements use CDATA for
			 * their data model, for these elements, CDATA must be handled
			 * differently by user agents. Markup and entities must be treated
			 * as raw text and passed to the application as is. The first
			 * occurrence of the character sequence "</" (end-tag open
			 * delimiter) is treated as terminating the end of the element's
			 * content. In valid documents, this would be the end tag for the
			 * element.
			 */

			// discard everything until endtag is hit (except
			// if it occurs in a comment.
			// reset the stream mark to here, since we know that we sucessfully
			// matched
			// a tag, and if we can't find the end tag, this is where we will
			// want
			// to roll back to.
			saveState();
			pushed.setLength(0);
			return findEndTag();
		}
		return MATCH;
	}

	// find an end tag, but beware of comments...
	// <script><!-- </script> -->foo</script>
	// beware markup in script strings:
	// </script>...document.write("</script>")foo</script>
	// TODO: do I need to worry about CDATA sections "<![CDATA[" ?
	int findEndTag() throws IOException {

		while ((numRead - lastMark) < safeReadAheadLimit) {
			int ch = next();
			if (ch == '<') {
				ch = next();
				// skip looking for end-tag in comments
				if (ch == '!') {
					int ret = readBang(true);
					if (ret == MATCH)
						continue;
					// yikes... what now? It wasn't a comment, but I can't get
					// back to the state I was at. Just continue from where I
					// am I guess...
					continue;
				}
				// did we match "</"
				if (ch != '/') {
					push(ch);
					continue;
				}
				int ret = readName(false);
				if (ret == MISMATCH)
					return MISMATCH;
				ch = nextSkipWS();
				if (ch != '>')
					return MISMATCH;
				return MATCH;
			} else if (ch == '\'' || ch == '"') {
				// read javascript string to avoid a false match.
				push(ch);
				int ret = readScriptString();
				// what to do about a non-match (non-terminated string?)
				// play it safe and index the rest of the data I guess...
				if (ret == MISMATCH)
					return MISMATCH;
			} else if (ch < 0) {
				return MISMATCH;
			}

		}
		return MISMATCH;
	}

	// read a string escaped by backslashes
	private int readScriptString() throws IOException {
		int quoteChar = next();
		if (quoteChar != '\'' && quoteChar != '"')
			return MISMATCH;

		while ((numRead - lastMark) < safeReadAheadLimit) {
			int ch = next();
			if (ch == quoteChar)
				return MATCH;
			else if (ch == '\\') {
				ch = next();
			} else if (ch < 0) {
				return MISMATCH;
			} else if (ch == '<') {
				eatSSI();
			}

		}
		return MISMATCH;
	}

	private int readName(boolean checkEscaped) throws IOException {
		StringBuilder builder = (checkEscaped && escapedTags != null) ? new StringBuilder() : null;
		int ch = next();
		if (builder != null)
			builder.append((char) ch);
		if (!isFirstIdChar(ch))
			return MISMATCH;
		ch = next();
		if (builder != null)
			builder.append((char) ch);
		while (isIdChar(ch)) {
			ch = next();
			if (builder != null)
				builder.append((char) ch);
		}
		if (ch != -1) {
			push(ch);

		}
		// strip off the trailing >
		if (builder != null && escapedTags.contains(builder.substring(0, builder.length() - 1))) {
			return MISMATCH;
		}
		return MATCH;
	}

	/***************************************************************************
	 * [10] AttValue ::= '"' ([^<&"] | Reference)* '"' | "'" ([^<&'] |
	 * Reference)* "'"
	 * 
	 * need to also handle unquoted attributes, and attributes w/o values:
	 * <td id=msviGlobalToolbar height="22" nowrap align=left>
	 * 
	 **************************************************************************/

	// This reads attributes and attempts to handle any
	// embedded server side includes that would otherwise
	// mess up the quote handling.
	// <a href="a/<!--#echo "path"-->">
	private int readAttr2() throws IOException {
		if ((numRead - lastMark < safeReadAheadLimit)) {
			int ch = next();
			if (!isFirstIdChar(ch))
				return MISMATCH;
			ch = next();
			while (isIdChar(ch) && ((numRead - lastMark) < safeReadAheadLimit)) {
				ch = next();
			}
			if (isSpace(ch))
				ch = nextSkipWS();

			// attributes may not have a value at all!
			// if (ch != '=') return MISMATCH;
			if (ch != '=') {
				push(ch);
				return MATCH;
			}

			int quoteChar = nextSkipWS();

			if (quoteChar == '"' || quoteChar == '\'') {
				while ((numRead - lastMark) < safeReadAheadLimit) {
					ch = next();
					if (ch < 0)
						return MISMATCH;
					else if (ch == '<') {
						eatSSI();
					} else if (ch == quoteChar) {
						return MATCH;
						// } else if (ch=='<') {
						// return MISMATCH;
					}

				}
			} else {
				// unquoted attribute
				while ((numRead - lastMark) < safeReadAheadLimit) {
					ch = next();
					if (ch < 0)
						return MISMATCH;
					else if (isSpace(ch)) {
						push(ch);
						return MATCH;
					} else if (ch == '>') {
						push(ch);
						return MATCH;
					} else if (ch == '<') {
						eatSSI();
					}

				}
			}
		}
		return MISMATCH;
	}

	// skip past server side include
	private int eatSSI() throws IOException {
		// at this point, only a "<" was read.
		// on a mismatch, push back the last char so that if it was
		// a quote that closes the attribute, it will be re-read and matched.
		int ch = next();
		if (ch != '!') {
			push(ch);
			return MISMATCH;
		}
		ch = next();
		if (ch != '-') {
			push(ch);
			return MISMATCH;
		}
		ch = next();
		if (ch != '-') {
			push(ch);
			return MISMATCH;
		}
		ch = next();
		if (ch != '#') {
			push(ch);
			return MISMATCH;
		}

		push('#');
		push('-');
		push('-');
		return readComment(false);
	}

	private int readProcessingInstruction() throws IOException {
		// "<?" has already been read
		while ((numRead - lastMark) < safeReadAheadLimit) {
			int ch = next();
			if (ch == '?' && peek() == '>') {
				next();
				return MATCH;
			} else if (ch == '/' && peek() == '>') {
				next();
				return MATCH;
			} else if (ch == -1) {
				return MISMATCH;
			}

		}
		return MISMATCH;
	}

	public int read() throws IOException {
		// TODO: Do we ever want to preserve CDATA sections?
		// where do we have to worry about them?
		// <![ CDATA [ unescaped markup ]]>
		if (numWhitespace > 0) {
			numEaten += numWhitespace;
			addOffCorrectMap(numReturned, numEaten);
			numWhitespace = 0;
		}
		numReturned++;
		// do not limit this one by the READAHEAD
		while (true) {
			int lastNumRead = numRead;
			int ch = next();

			switch (ch) {
			case '&':
				saveState();
				ch = readEntity();
				if (ch >= 0)
					return ch;
				if (ch == MISMATCH) {
					restoreState();

					return '&';
				}
				break;

			case '<':
				saveState();
				ch = next();
				int ret = MISMATCH;
				if (ch == '!') {
					ret = readBang(false);
				} else if (ch == '/') {
					ret = readName(true);
					if (ret == MATCH) {
						ch = nextSkipWS();
						ret = ch == '>' ? MATCH : MISMATCH;
					}
				} else if (isAlpha(ch)) {
					push(ch);
					ret = readTag();
				} else if (ch == '?') {
					ret = readProcessingInstruction();
				}

				// matched something to be discarded, so break
				// from this case and continue in the loop
				if (ret == MATCH) {
					// break;//was
					// return whitespace from
					numWhitespace = (numRead - lastNumRead) - 1;// tack on the
																// -1 since we
																// are returning
																// a space right
																// now
					return ' ';
				}

				// didn't match any HTML constructs, so roll back
				// the stream state and just return '<'
				restoreState();
				return '<';

			default:
				return ch;
			}

		}

	}

	public int read(char cbuf[], int off, int len) throws IOException {
		int i = 0;
		for (i = 0; i < len; i++) {
			int ch = read();
			if (ch == -1)
				break;
			cbuf[off++] = (char) ch;
		}
		if (i == 0) {
			if (len == 0)
				return 0;
			return -1;
		}
		return i;
	}

	public void close() throws IOException {
		input.close();
	}

	private static final HashMap<String, Character> entityTable;
	static {
		entityTable = new HashMap<String, Character>();
		// entityName and entityVal generated from the python script
		// included in comments at the end of this file.
		final String[] entityName = { "zwnj", "aring", "gt", "yen", "ograve", "Chi", "delta", "rang", "sup", "trade", "Ntilde", "xi", "upsih", "nbsp", "Atilde", "radic", "otimes", "aelig", "oelig", "equiv", "ni", "infin", "Psi", "auml", "cup", "Epsilon", "otilde", "lt", "Icirc",
				"Eacute", "Lambda", "sbquo", "Prime", "prime", "psi", "Kappa", "rsaquo", "Tau", "uacute", "ocirc", "lrm", "zwj", "cedil", "Alpha", "not", "amp", "AElig", "oslash", "acute", "lceil", "alefsym", "laquo", "shy", "loz", "ge", "Igrave", "nu", "Ograve", "lsaquo",
				"sube", "euro", "rarr", "sdot", "rdquo", "Yacute", "lfloor", "lArr", "Auml", "Dagger", "brvbar", "Otilde", "szlig", "clubs", "diams", "agrave", "Ocirc", "Iota", "Theta", "Pi", "zeta", "Scaron", "frac14", "egrave", "sub", "iexcl", "frac12", "ordf", "sum", "prop",
				"Uuml", "ntilde", "atilde", "asymp", "uml", "prod", "nsub", "reg", "rArr", "Oslash", "emsp", "THORN", "yuml", "aacute", "Mu", "hArr", "le", "thinsp", "dArr", "ecirc", "bdquo", "Sigma", "Aring", "tilde", "nabla", "mdash", "uarr", "times", "Ugrave", "Eta",
				"Agrave", "chi", "real", "circ", "eth", "rceil", "iuml", "gamma", "lambda", "harr", "Egrave", "frac34", "dagger", "divide", "Ouml", "image", "ndash", "hellip", "igrave", "Yuml", "ang", "alpha", "frasl", "ETH", "lowast", "Nu", "plusmn", "bull", "sup1", "sup2",
				"sup3", "Aacute", "cent", "oline", "Beta", "perp", "Delta", "there4", "pi", "iota", "empty", "euml", "notin", "iacute", "para", "epsilon", "weierp", "OElig", "uuml", "larr", "icirc", "Upsilon", "omicron", "upsilon", "copy", "Iuml", "Oacute", "Xi", "kappa",
				"ccedil", "Ucirc", "cap", "mu", "scaron", "lsquo", "isin", "Zeta", "minus", "deg", "and", "tau", "pound", "curren", "int", "ucirc", "rfloor", "ensp", "crarr", "ugrave", "exist", "cong", "theta", "oplus", "permil", "Acirc", "piv", "Euml", "Phi", "Iacute", "quot",
				"Uacute", "Omicron", "ne", "iquest", "eta", "rsquo", "yacute", "Rho", "darr", "Ecirc", "Omega", "acirc", "sim", "phi", "sigmaf", "macr", "thetasym", "Ccedil", "ordm", "uArr", "forall", "beta", "fnof", "rho", "micro", "eacute", "omega", "middot", "Gamma", "rlm",
				"lang", "spades", "supe", "thorn", "ouml", "or", "raquo", "part", "sect", "ldquo", "hearts", "sigma", "oacute" };
		final char[] entityVal = { 8204, 229, 62, 165, 242, 935, 948, 9002, 8835, 8482, 209, 958, 978, 160, 195, 8730, 8855, 230, 339, 8801, 8715, 8734, 936, 228, 8746, 917, 245, 60, 206, 201, 923, 8218, 8243, 8242, 968, 922, 8250, 932, 250, 244, 8206, 8205, 184, 913, 172, 38,
				198, 248, 180, 8968, 8501, 171, 173, 9674, 8805, 204, 957, 210, 8249, 8838, 8364, 8594, 8901, 8221, 221, 8970, 8656, 196, 8225, 166, 213, 223, 9827, 9830, 224, 212, 921, 920, 928, 950, 352, 188, 232, 8834, 161, 189, 170, 8721, 8733, 220, 241, 227, 8776, 168,
				8719, 8836, 174, 8658, 216, 8195, 222, 255, 225, 924, 8660, 8804, 8201, 8659, 234, 8222, 931, 197, 732, 8711, 8212, 8593, 215, 217, 919, 192, 967, 8476, 710, 240, 8969, 239, 947, 955, 8596, 200, 190, 8224, 247, 214, 8465, 8211, 8230, 236, 376, 8736, 945, 8260,
				208, 8727, 925, 177, 8226, 185, 178, 179, 193, 162, 8254, 914, 8869, 916, 8756, 960, 953, 8709, 235, 8713, 237, 182, 949, 8472, 338, 252, 8592, 238, 933, 959, 965, 169, 207, 211, 926, 954, 231, 219, 8745, 956, 353, 8216, 8712, 918, 8722, 176, 8743, 964, 163, 164,
				8747, 251, 8971, 8194, 8629, 249, 8707, 8773, 952, 8853, 8240, 194, 982, 203, 934, 205, 34, 218, 927, 8800, 191, 951, 8217, 253, 929, 8595, 202, 937, 226, 8764, 966, 962, 175, 977, 199, 186, 8657, 8704, 946, 402, 961, 181, 233, 969, 183, 915, 8207, 9001, 9824,
				8839, 254, 246, 8744, 187, 8706, 167, 8220, 9829, 963, 243 };
		for (int i = 0; i < entityName.length; i++) {
			entityTable.put(entityName[i], new Character(entityVal[i]));
		}
		// special-case nbsp to a simple space instead of 0xa0
		entityTable.put("nbsp", new Character(' '));
	}

}
