package org.onetwo.plugins.jdoc.Lexer;

import static org.onetwo.common.lexer.JLexerUtils.isCommentStart;
import static org.onetwo.common.lexer.JLexerUtils.isCommentsStart;
import static org.onetwo.common.lexer.JLexerUtils.isFirstIdentifier;
import static org.onetwo.common.lexer.JLexerUtils.isIdentifier;
import static org.onetwo.common.lexer.JLexerUtils.isJavaNumberChar;
import static org.onetwo.common.lexer.JLexerUtils.isNumberChar;
import static org.onetwo.common.lexer.JLexerUtils.isStartChar;
import static org.onetwo.common.lexer.JLexerUtils.isWhiteSpace;

import org.onetwo.common.lexer.AbstractLexer;
import org.onetwo.common.lexer.JLexerException;
import org.onetwo.common.lexer.SourceReader;

public class JLexer extends AbstractLexer<JToken> {
	
	
	private JKeyWords keyWords = JKeyWords.INSTANCE;
//	private JToken token;
	
//	private int markPos;
	
	public JLexer(SourceReader input){
		super(input);
	}
	 
	
	public boolean nextToken(){
		this.clearCharBuf();
		while(true){
			if(isEOF()){
				setToken(JToken.EOF);
				return false;
			}
			
			if(isWhiteSpace(ch) || isStartChar(ch)){
				scanNextChar();
				continue;
			}
			
			if(isFirstIdentifier(ch)){
				scanIdentifier();
				return true;
			}
			
			if(ch=='/'){
				final char ch1 = ch;
				scanNextChar();
				
				if(ch=='*'){
					final char ch2 = ch;
					scanNextChar();
					if(ch=='*'){
						saveChar(ch1, ch2, ch);
						return scanJavaDoc();
//						return true;
					}else{
						saveChar(ch1, ch2);
						scanMutiComments();
					}
				}else if(ch=='/'){
					//scanComment();
					saveChar(ch1, ch);
					while(true){
						scanNextChar();
						if(ch=='\r' || ch=='\n'){
							break;
						}
						saveChar();
					}
					setToken(JToken.COMMENT);
					saveChar();
//					scanNextChar();
//					return true;
				}else{
					setToken(JToken.UNKNOW);
				}
			}else if(ch=='{'){
				setToken(JToken.LBRACE);
			}else if(ch=='}'){
				setToken(JToken.RBRACE);
			}else if(ch=='('){
				setToken(JToken.LPARENT);
			}else if(ch==')'){
				setToken(JToken.RPARENT);
			}else if(ch=='['){
				setToken(JToken.LBRACKET);
			}else if(ch==']'){
				setToken(JToken.RBRACKET);
			}else if(ch=='<'){
				setToken(JToken.LANGLE);
			}else if(ch=='>'){
				setToken(JToken.RANGLE);
			}else if(ch==';'){
				setToken(JToken.SEMI);
			}else if(ch==','){
				setToken(JToken.COMMA);
			}else if(ch=='@'){
				setToken(JToken.AT);
			}else if(ch=='='){
				setToken(JToken.ASSIGN);
			}else if(ch=='.'){
				setToken(JToken.DOT);
			}else if(ch=='\"'){
				scanString();
				return true;
			}else if(isNumberChar(ch)){
				scanNumber();
				return true;
			}/*else if(isEOF()){
				token = JToken.EOF;
			}*/else{
				setToken(JToken.UNKNOW);
			}
//			if(token!=JToken.UNKNOW){
//				saveChar();
//			}
			saveChar();
			scanNextChar();

			//TODO
			if(getToken()==JToken.UNKNOW || getToken()==JToken.COMMENT || getToken()==JToken.MUTI_COMMENTS){
				return this.nextToken();
			}
			return true;
		}
	}
	
	protected void scanNumber(){
		if(!isNumberChar(ch)){
			throw new JLexerException("not a number char : " + ch);
		}
		saveChar();
		while(true){
			scanNextChar();
			if((!isJavaNumberChar(ch))){
				break;
			}
			saveChar();
		}
		setToken(JToken.NUMBER);
	}
	
	protected void scanString(){
		if(ch!='\"'){
			throw new JLexerException("not a string start : " + ch);
		}
		saveChar();
		while(true){
			scanNextChar();
			saveChar();
			if(ch=='\"'){
				break;
			}
		}
		scanNextChar();
		setToken(JToken.JString);
	}

	protected boolean scanJavaDoc(){
		if(!isCommentStart(getStringValue())){
			throw new JLexerException();
		}
		scanNextChar();
		if(ch=='/'){
			saveChar();
			setToken(JToken.UNKNOW);// ignore this /**/
			return this.nextToken();
		}
		
		saveChar();
		while(true){
			scanNextChar();
//			if(isWhiteSpace(ch))
//				continue;
			if(isStartChar(ch)){
				final char ch1 = ch;
				scanNextChar();
				if(ch=='/'){
					saveChar(ch1, ch);
					scanNextChar();
					break;
				}
			}
			saveChar();
		}
		setToken(JToken.JAVADOC);
		return true;
	}
	protected void scanMutiComments(){
		if(!isCommentsStart(getStringValue())){
			throw new JLexerException("parse error: "+getStringValue());
		}
		saveChar();
		while(true){
			scanNextChar();
//			if(isWhiteSpace(ch))
//				continue;
			if(isStartChar(ch)){
				final char ch1 = ch;
				scanNextChar();
				if(ch=='/'){
					saveChar(ch1, ch);
					scanNextChar();
					break;
				}
			}
			saveChar();
		}
		setToken(JToken.MUTI_COMMENTS);
	}
	
	
	protected void scanIdentifier(){
		if(!isFirstIdentifier(ch)){
			throw new JLexerException();
		}
		saveChar();
		while(true){
			scanNextChar();
//			if(getStringValue().equals("Controlle")){
//				System.out.println("debug");
//			}
			if(!isIdentifier(ch)){
				break;
			}
			saveChar();
		}
		String id = getStringValue();
		JToken t = keyWords.getTokenByKeyWord(id);
		if(t!=null){
			setToken(t);
		}else{
			setToken(JToken.IDENTIFIER);
		}
	}
	

	public JKeyWords getKeyWords() {
		return keyWords;
	}

}
