package org.onetwo.common.lexer;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

abstract public class AbstractLexer<T> {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected final SourceReader reader;
	
	private StringBuilder text = new StringBuilder();
	private boolean debug = true;
	protected char ch;
//	private int pos;
	
	private StringBuilder charBuf = new StringBuilder();
//	private JTokenValue<T> tokenValue;
	private T token;
	
//	private int markPos;
	
	public AbstractLexer(SourceReader input){
		this.reader = input;
		scanNextChar();
	}
	 
	
	public boolean isDebug() {
		return debug;
	}


	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	protected char scanNextChar(){
		if(this.reader.isEOF()){
			ch = JLexerUtils.EOI;
			return ch;
		}
		ch = this.reader.readNext();
		if(debug){
			this.text.append(ch);
		}
		return ch;
	}
	
	protected boolean isEOF(){
		return JLexerUtils.isEOF(ch);
	}
	
	abstract public boolean nextToken();
	
	public T getToken(){
		return token;
	}
	
	/*public JTokenValue<T> getTokenValue(){
		return tokenValue;
	}*/
	
	public void setToken(T token) {
		this.token = token;
//		tokenValue = new JTokenValue<T>(token, getStringValue());
	}

	public String getStringValue(){
		String str = charBuf.toString();
		return str;
	}
	

	protected void saveChar(){
		this.charBuf.append(ch);
	}
	
	protected void saveChar(char... chars){
		for(char c : chars){
			this.charBuf.append(c);
		}
	}
	
	protected void clearCharBuf(){
		if(this.charBuf.length()==0)
			return ;
		this.charBuf.delete(0, this.charBuf.length());
	}

	public void reset() {
		this.reader.reset();
		scanNextChar();
		this.text.delete(0, text.length());
		this.clearCharBuf();
	}


	abstract public LexerKeywords<T> getKeyWords();
}
