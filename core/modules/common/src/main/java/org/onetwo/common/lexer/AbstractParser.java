package org.onetwo.common.lexer;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.parser.SqlTokenKey;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public abstract class AbstractParser<T> {

	protected final AbstractLexer<T> lexer;
	
	public AbstractParser(AbstractLexer<T> lexer) {
		super();
		this.lexer = lexer;
	}

	public void ignoreBlock(int count, T start, T end){
		int braceCount = count;
		while(this.throwIfNoNextToken()){
			if (tokenIs(start)) {
				braceCount++;
			} else if (tokenIs(end)) {
				braceCount--;
			}else{
				if(braceCount==0){
					//不检查extends implements 
					continue;
				}
				
			}
			if(braceCount==0)
				break;
		}
	}

	public boolean throwIfNoNextToken(){
		if(!lexer.nextToken())
			throw new JSyntaxException("java syntax error, it may be not a completion java code!");
		return true;
	}

	public boolean throwIfNextTokenIsNot(T token, Object target){
		this.throwIfNoNextToken();
		if(token!=lexer.getToken())
			throw new JSyntaxException("java syntax error, "+(target!=null?target:"it")+" should be "+token+", but it's "+lexer.getToken()+"");
		return true;
	}
	public boolean throwIfNextTokenIsNotOneOf(T... tokens){
		if(!nextTokenIsOneOf(tokens))
			throw new JSyntaxException("java syntax error, it should be "+StringUtils.join(tokens, ",")+", but it's "+lexer.getToken()+"");
		return true;
	}
	public boolean nextTokenIsOneOf(T... tokens){
		this.throwIfNoNextToken();
		return tokenIsOneOf(tokens);
	}
	public boolean tokenIsOneOf(T... tokens){
		for(T token : tokens){
			if(token==lexer.getToken())
				return true;
		}
		return false;
	}
	
	public boolean tokenIs(T token){
		return token==lexer.getToken();
	}
	public boolean nextTokenIs(T token){
		throwIfNoNextToken();
		return lexer.getToken()==token;
	}
	
	public String stringValue(){
		return lexer.getStringValue();
	}


	public AbstractLexer<T> getLexer() {
		return (AbstractLexer<T>)lexer;
	}
	
	public List<T> nextAllTheseTokens(T...definedTokens){
		List<T> kws = new ArrayList<T>();
		while(true){
			if(ArrayUtils.contains(definedTokens, lexer.getToken())){
				kws.add(lexer.getToken());
				throwIfNoNextToken();
			}else{
				break;
			}
		}
		return kws;
	}
	
	public JTokenValueCollection<T> nextAllTokensUntil(T... tokens){
		JTokenValueCollection<T> tks = new JTokenValueCollection<T>();
		
		if(ArrayUtils.contains(tokens, lexer.getToken()))
			return tks;
		tks.addJTokenValue(lexer.getToken(), stringValue());
		
		while(this.getLexer().nextToken()){//true
			if(ArrayUtils.contains(tokens, lexer.getToken())){
				break;
			}else{
				tks.addJTokenValue(lexer.getToken(), stringValue());
//				throwIfNoNextToken();
			}
		}
		return tks;
	}

	public List<T> nextAllKeyWordTokens(){
		List<T> kws = new ArrayList<T>();
		while(true){
			if(lexer.getKeyWords().isKeyWord(lexer.getToken())){
				kws.add(lexer.getToken());
				throwIfNoNextToken();
			}else{
				break;
			}
		}
		return kws;
	}

	public static class JTokenValue<T> {
		public final T token;
		public final String value;
		public JTokenValue(T token, String value) {
			super();
			this.token = token;
			this.value = value;
		}
		public T getToken() {
			return token;
		}
		public String getValue() {
			return value;
		}
		public String toString(){
			return LangUtils.append(token, ": " + value);
		}
	}
	
	public static class JTokenValueCollection<T> {
		private List<JTokenValue<T>> tokenValues = LangUtils.newArrayList();

		public boolean contains(T token){
			for(JTokenValue<T> tv : tokenValues){
				if(tv.token==token)
					return true;
			}
			return false;
		}
		public JTokenValue<T> getTokenValue(T token){
			for(JTokenValue<T> tv : tokenValues){
				if(tv.token==token)
					return tv;
			}
			return null;
		}
		
		public void addJTokenValue(T token, String value){
			this.tokenValues.add(new JTokenValue<T>(token, value));
		}
		
		public void addJTokenValue(JTokenValue<T> tv){
			this.tokenValues.add(tv);
		}
		
		public void addJTokenValue(int index, JTokenValue<T> tv){
			this.tokenValues.add(index, tv);
		}
		
		public JTokenValue<T> getLast(){
			if(LangUtils.isEmpty(tokenValues))
				return null;
			return this.tokenValues.get(this.tokenValues.size()-1);
		}
		
		public JTokenValue<T> getFirst(){
			if(LangUtils.isEmpty(tokenValues))
				return null;
			return this.tokenValues.get(0);
		}
		
		public boolean startWith(SqlTokenKey token){
			return  isEmpty()?false:(token == getFirst().getToken());
		}
		
		public boolean endWith(SqlTokenKey token){
			return isEmpty()?false:( token == getLast().getToken() );
		}
		
		public JTokenValue<T> remove(int index){
			if(LangUtils.isEmpty(tokenValues))
				return null;
			return this.tokenValues.remove(index);
		}
		
		public JTokenValue<T> removeLast(){
			if(LangUtils.isEmpty(tokenValues))
				return null;
			return this.tokenValues.remove(this.tokenValues.size()-1);
		}
		
		
		public String getValuesExceptLast(){
			JTokenValue<T> last = getLast();
			StringBuilder value = new StringBuilder();
			for(JTokenValue<T> tv : tokenValues){
				if(!tv.equals(last)){
					value.append(tv.value);
				}
			}
			return value.toString();
		}
		
		public String getValues(String op){
			StringBuilder value = new StringBuilder();
			int index = 0;
			for(JTokenValue<T> tv : tokenValues){
				if(index!=0){
					value.append(op);
				}
				value.append(tv.value);
				index++;
			}
			return value.toString();
		}
		
		public boolean isEmpty(){
			return this.tokenValues.isEmpty();
		}
		
		public void clear(){
			this.tokenValues.clear();
		}
		
		public List<JTokenValue<T>> getTokenValues() {
			return tokenValues;
		}
		
		public String getVauesWithReplace(String op, T token, String replace){
			StringBuilder value = new StringBuilder();
			int index = 0;
			for(JTokenValue<T> tv : this.tokenValues){
				if(index!=0){
					value.append(op);
				}
				if(tv.getToken()==token){
					value.append(replace);
				}else{
					value.append(tv.getValue());
				}
				index++;
			}
			return value.toString();
		}
		
		public JTokenValueCollection<T> clone(){
			JTokenValueCollection<T> c = new JTokenValueCollection<T>();
			for(JTokenValue<T> t : this.tokenValues){
				c.tokenValues.add(t);
			}
			return c;
		}
		
		public String toString(){
			return getValues(" ");
		}
		
	}
}
