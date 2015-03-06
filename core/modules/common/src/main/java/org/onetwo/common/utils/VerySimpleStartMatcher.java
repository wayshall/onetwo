package org.onetwo.common.utils;

import java.util.Map;
import java.util.WeakHashMap;


public class VerySimpleStartMatcher {
	public static VerySimpleStartMatcher createTokens(String op, String... tokens){
		VerySimpleStartMatcher m = new VerySimpleStartMatcher(op, tokens);
		return m;
	}
	public static VerySimpleStartMatcher create(String exp){
		return create(exp, true);
	}
	public static VerySimpleStartMatcher create(String exp, boolean cache){
		VerySimpleStartMatcher m = cache?caches.get(exp):null;
		if(m==null)
			m = new VerySimpleStartMatcher(exp);
		if(cache){
			caches.put(m.getExp(), m);
		}
		return m;
	}
	
	private static final Map<String, VerySimpleStartMatcher> caches = new WeakHashMap<String, VerySimpleStartMatcher>();
	
	private String _exp;
	private int start = -1;
	private int end = -1;
	private String separator;
	

	public VerySimpleStartMatcher(String separator, String... tokens){
		Assert.notNull(separator);
		Assert.notEmpty(tokens);
		String exp = StringUtils.join((String[])tokens, separator);
		this.separator = separator;
		_parse(exp);
	}
	
	VerySimpleStartMatcher(String exp){
		_parse(exp);
	}
	
	public void _parse(String exp){
		this._exp = exp.toLowerCase();
		if(_exp.startsWith("*")){
			start = 0;
			this._exp = this._exp.substring(start+1);
		}
		if(_exp.endsWith("*")){
			end = _exp.length()-1;
			this._exp = this._exp.substring(0, end);
		}
	}


	public boolean match(String str){
		Assert.notNull(str);
		if(separator!=null){
			String[] strs = StringUtils.split(str, separator);
			str = StringUtils.join(strs, separator);
		}
		str = str.toLowerCase();
		if(start!=-1 && end!=-1){
			return str.indexOf(_exp)!=-1;
		}else if(start!=-1){
			return str.endsWith(_exp);
		}else if(end!=-1){
			return str.startsWith(_exp);
		}else{
			return _exp.equals(str);
		}
	}
	String getExp() {
		return _exp;
	}
}
