package org.onetwo.common.lexer;


public final class JLexerUtils {

//	public static final int EOF = -1;

    public final static byte EOI = 0x1A;
	
	private static final boolean[] WHITE_SPACE = new boolean[256];
	static {
		WHITE_SPACE[' '] = true;
		WHITE_SPACE['\n'] = true;
		WHITE_SPACE['\r'] = true;
		WHITE_SPACE['\t'] = true;
		WHITE_SPACE['\f'] = true;
		WHITE_SPACE['\b'] = true;
	}
	
	private static final boolean[] IDENTIFIERS = new boolean[256];
	static {
		for(char c=0; c<IDENTIFIERS.length; c++){
			if(c>='a' && c<='z'){
				IDENTIFIERS[c] = true;
			}else if(c>='A' && c<='Z'){
				IDENTIFIERS[c] = true;
			}else if(c>='0' && c<='9'){
				IDENTIFIERS[c]= true;
			}
		}
		IDENTIFIERS['_'] = true;
	}
	
	private static final boolean[] NUMBER_CHARS = new boolean[256];
	static {
		for(char c=0; c<NUMBER_CHARS.length; c++){
			if(c>='0' && c<='9'){
				NUMBER_CHARS[c]= true;
			}
		}
		NUMBER_CHARS['-'] = true;
		NUMBER_CHARS['.'] = true;
	}
	
	private static final boolean[] JNUMBER_CHARS = new boolean[256];
	static {
		for(char c=0; c<JNUMBER_CHARS.length; c++){
			if(c>='0' && c<='9'){
				JNUMBER_CHARS[c]= true;
			}
		}
		JNUMBER_CHARS['.'] = true;
		JNUMBER_CHARS['f'] = true;
		JNUMBER_CHARS['F'] = true;
		JNUMBER_CHARS['d'] = true;
		JNUMBER_CHARS['D'] = true;
		JNUMBER_CHARS['l'] = true;
		JNUMBER_CHARS['L'] = true;
	}
	
	private static final boolean[] FIRST_IDENTIFIERS = new boolean[256];
	static {
		for(char c=0; c<FIRST_IDENTIFIERS.length; c++){
			if(c>='a' && c<='z'){
				FIRST_IDENTIFIERS[c] = true;
			}else if(c>='A' && c<='Z'){
				FIRST_IDENTIFIERS[c] = true;
			}
		}
		FIRST_IDENTIFIERS['_'] = true;
	}
	
	public static boolean isWhiteSpace(char ch){
		return ch < WHITE_SPACE.length && WHITE_SPACE[ch];
	}
	
	public static boolean isNumberChar(char ch){
		try {
			return NUMBER_CHARS[ch];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	} 
	
	public static boolean isJavaNumberChar(char ch){
		return JNUMBER_CHARS[ch];
	} 
	
	public static boolean isIdentifier(char ch){
		return ch <IDENTIFIERS.length && IDENTIFIERS[ch];
	}
	
	public static boolean isFirstIdentifier(char ch){
		return ch <FIRST_IDENTIFIERS.length && FIRST_IDENTIFIERS[ch];
	}
	
	public static boolean isCommentStart(String str){
		return "/**".equals(str);
	}
	
	public static boolean isCommentsStart(String str){
		return "/*".equals(str);
	}
	
	public static boolean isCommentEnd(String str){
		return "*/".equals(str);
	}
	
	public static boolean isStartChar(char ch){
		return '*' == ch;
	}
	
	public static boolean isEOF(char ch){
		return ch==EOI;
	}
	
	private JLexerUtils(){
	}
}
