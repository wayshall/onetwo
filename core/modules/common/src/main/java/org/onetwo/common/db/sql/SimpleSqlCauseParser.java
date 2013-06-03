package org.onetwo.common.db.sql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SToken;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.Stringer;
import org.onetwo.common.utils.list.L;

@SuppressWarnings("unchecked")
public class SimpleSqlCauseParser implements SqlCauseParser {

	private static final String JDBC_PARAM_PLACEHODER = ConditionToken.JDBC_PARAM_PLACEHODER;
	public static final String PARENTHESIS_LEFT = "(";
	public static final String PARENTHESIS_RIGHT = ")";

	public static final List<String> KEYWORDS = L.aslist("and", "or", "where");
	public static final List<String> OPERATORS = L.aslist("=", "<", ">", ">=", "<=", "!=", "<>", "in", "like");
	public static final List<String> RETAIN_SEPS = L.aslistIfNull(false, " ", ",", "(", ")", "=", "<", ">", ">=", "<=", "!=", "<>", " in", " like ");//seperator

//	public static final int DEFAULT_CACHE_LIMIT = 256;
	
	protected List<String> operators = OPERATORS;
	protected List<String> retainSeperators = RETAIN_SEPS;
	protected Map<Integer, List<SToken>> caches;
	protected int cacheLimit = -1;//-1 is no limited
	

	public SimpleSqlCauseParser() {
		this(-1);
	}
	@SuppressWarnings("serial")
	public SimpleSqlCauseParser(int cacheLimit) {
		this.cacheLimit = cacheLimit;
		if(isCacheEnable()){
			caches = new LinkedHashMap<Integer, List<SToken>>(cacheLimit, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<Integer, List<SToken>> eldest) {
					return size() > getCacheLimit();
				}
			};
			
//			caches = new ConcurrentHashMap<Integer, List<SToken>>();
		}
	}
	
	public int getCacheLimit() {
		return cacheLimit;
	}


	protected boolean isCacheEnable(){
		return cacheLimit>0;
	}
	

	/*@Override
	public List<SToken> parseSql(String originalSql) {
		int key = originalSql.hashCode();
		List<SToken> tokens = null;
		if(isCacheEnable()){
			if(caches.containsKey(key))
				return caches.get(key);
		}
		tokens = _parseSql(originalSql);
		
		if(isCacheEnable()){
			caches.put(key, tokens);
		}
		return tokens;
	}*/
	@Override
	public List<SToken> parseSql(String originalSql) {
		if(!isCacheEnable())
			return _parseSql(originalSql);
		
		int key = originalSql.hashCode();
		List<SToken> tokens = null;
		synchronized (this.caches) {
			tokens = this.caches.get(key);
			if(tokens==null){
				tokens = _parseSql(originalSql);
				this.caches.put(key, tokens);
			}
		}
		return tokens;
	}

	public List<SToken> _parseSql(String originalSql) {
		Assert.hasText(originalSql);
		
		if(UtilTimerStack.isActive()){
			UtilTimerStack.push("parseSql");
		}
		
		Stringer sg = Stringer.wrap(originalSql, null, getRetainSeperators());
		String[] strs = sg.getArrays();
		
//		LangUtils.println("tokens: ", strs);
		
		List<SToken> tokens = new ArrayList<SToken>();
		SToken token = null;
		ConditionToken cond = null;
		int conditionCount = 0;
		tokens = new ArrayList<SToken>();
		for (int i = 0; i < strs.length;) {
			token = new SToken(strs[i], i);
			
//			System.out.println("--:" + token);
			
			try {
				cond = createCondition(tokens, token, strs, conditionCount);
			} catch (Exception e) {
				throw new BaseException("parse condition error : " + LangUtils.toString(tokens), e);
			}
			if(cond!=null){
				cond.setIndex(conditionCount);
				tokens.add(cond);
				i = cond.getEndStrIndex()+1;
				conditionCount++;
				
				/*System.out.println("add condition["+conditionCount+"] : " + cond);
				System.out.println("tokens ["+tokens.size()+"]: " + tokens);
				System.out.println("\n\n");*/
				
			}else{
				tokens.add(token);
				i++;
			}
		}
		

		if(UtilTimerStack.isActive()){
			UtilTimerStack.pop("parseSql");
		}
		
//		LangUtils.println("conditions size:"+conditions.size());
		return tokens;
	}
	
	protected List<String> getKeyWords(){
		return KEYWORDS;
	}

	protected QueryToken findOp(List<SToken> tokens, int currentIndex){
		if(currentIndex>tokens.size()-1)
			currentIndex = tokens.size()-1;
		String op = "";
//		int matchFlag = -1;// 0:match, 1:end match
		int startIndex = -1;
		int endIndex = -1;
		for(int i=currentIndex; i>0; i--){
			SToken token = tokens.get(i);
//			if(token.isEmptyStr())
			if(token.isBlankStr())
				continue;
			if(Condition.class.isInstance(token))
				break;
			String str = token.getName();
			if(getKeyWords().contains(str.toLowerCase())){
				return null;
			}
			if(!getOperators().contains(str.toLowerCase())){
				if(startIndex!=-1)
					endIndex = i+1;
				else
					continue;
			}
			if(startIndex==-1){
				startIndex = i;
			}else if(endIndex!=-1){
				QueryToken qt = new QueryToken(op, tokens.get(startIndex).getStrIndex());
				qt.setTokenIndex(endIndex);
				qt.setEndStrIndex(tokens.get(endIndex).getStrIndex());
				return qt;
			}
			op = str + op;
		}
		return null;
	}
	
	/*********
	 * if use function on the field name, condtion name will be included the function name
	 * @param tokens
	 * @param currentIndex
	 * @return
	 */
	protected QueryToken findName(List<SToken> tokens, int currentIndex){
		if(currentIndex>tokens.size()-1)
			currentIndex = tokens.size()-1;
		QueryToken nameToken = null;
		String name = "";
		int functionFlag = -1;//falg => -1:not function, 0:), 1:(, 2:functionName
		for(int i=currentIndex-1; i>0; i--){
			SToken token = tokens.get(i);
			if(Condition.class.isInstance(token))
				break;
			String str = token.getName();
			if(StringUtils.isBlank(str))
				continue;
			if(str.trim().equals(PARENTHESIS_RIGHT)){
				functionFlag = 0;
			}else if(str.trim().equals(PARENTHESIS_LEFT)){
				functionFlag = 1;
			}else if(functionFlag==1){
				functionFlag = 2;
			}
			
			name = str + name;
			if(functionFlag==-1 || functionFlag==2){
				nameToken = new QueryToken(name, token.getStrIndex());
				nameToken.setTokenIndex(i);
				break;
			}
		}
		return nameToken;
	}
	

	protected SToken findFullVarname(SToken varnameToken, QueryToken opToken, String[] tokenStrs){
		SToken nameToken = null;
		String name = "";
		String token = null;
		int functionFlag = -1;//falg => -1:not function, 0:(, 1:), 2:functionName
		for(int i=opToken.getEndStrIndex()+1; i<tokenStrs.length; i++){
			token = tokenStrs[i];
			if(getOperators().contains(token))//<=,多个操作符
				continue;
			if(token.trim().equals(PARENTHESIS_LEFT)){
				functionFlag = 0;
			}else if(token.trim().equals(PARENTHESIS_RIGHT)){
				functionFlag = 1;
			}
			
			if(i==varnameToken.getStrIndex()){
				if(functionFlag==0){
					token = JDBC_PARAM_PLACEHODER;
				}else{
					return null;
				}
			}
			
			name += token;
			if(functionFlag==1){
				nameToken = new SToken(name, i);
				break;
			}
		}
		return nameToken;
	}
	
	protected SToken findPreNotblankToken(List<String> strs, int currentIndex, int offsize){
		int count = 0;
		if(currentIndex>strs.size()-1)
			currentIndex = strs.size()-1;
		for(int i=currentIndex; i>0; i--){
			if(StringUtils.isBlank(strs.get(i)))
				continue;
			if(count==offsize){
				SToken t = new SToken(strs.get(i), i);
				return t;
			}
			count++;
		}
		return null;
	}
	
	protected List<String> getSeperators(){
		return null;
	}
	
	protected List<String> getRetainSeperators(){
		return retainSeperators;
	}
	
	protected List<String> getOperators(){
		return OPERATORS;
	}
	
	protected String getNamedParameterPrefix(){
		return ":";
	}
	
	protected ConditionToken newCondition(SToken token, QueryToken opToken){
		return new ConditionToken();
	}
	
	protected ConditionToken createCondition(List<SToken> tokens, SToken stoken, String[] strs, int conditionCount){
		String token = stoken.getName();
		ConditionToken cond = null;
		String varPrefix = getNamedParameterPrefix();
		int index = conditionCount;
		boolean named = token.startsWith(varPrefix);
		boolean paramToken = token.equals(JDBC_PARAM_PLACEHODER) || named;
		if(paramToken){
			QueryToken opToken = findOp(tokens, tokens.size());
			cond = newCondition(stoken, opToken);
			/*if(token.startsWith(":maxtime")){
				System.out.println("");
			}*/
			if (opToken!=null) {
				QueryToken nameToken = findName(tokens, opToken.getTokenIndex());
				cond.setName(nameToken.getName());
				cond.setOp(opToken.getName());
				
				if(named){
					cond.setVarname(token.substring(varPrefix.length()));
				}else{
					cond.setVarname(token+index);
				}
				
				SToken fullVar = findFullVarname(stoken, opToken, strs);
				
				cond.setStrIndex(nameToken.getStrIndex());
				if(fullVar!=null){
					cond.setEndStrIndex(fullVar.getStrIndex());
					cond.setActualPlaceHolder(fullVar.getName());
				}else{
					cond.setEndStrIndex(stoken.getStrIndex());
					cond.setActualPlaceHolder(JDBC_PARAM_PLACEHODER);
				}
				
				LangUtils.remove(tokens, nameToken.getTokenIndex(), tokens.size());
			}else{
				if(named){
					cond.setName(token.substring(varPrefix.length()));
				}else{
					cond.setName(token+index);// ?0, ?1 
				}
				cond.setOp(null);
				cond.setInfixOperator(false);
				cond.setVarname(cond.getName());
				cond.setStrIndex(stoken.getStrIndex());
				cond.setEndStrIndex(stoken.getStrIndex());
				cond.setActualPlaceHolder(JDBC_PARAM_PLACEHODER);
			}
		}
		return cond;
	}
}
