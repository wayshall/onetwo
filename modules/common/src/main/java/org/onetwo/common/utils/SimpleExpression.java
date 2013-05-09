package org.onetwo.common.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("rawtypes")
public class SimpleExpression implements Expression {
 
	public static final Expression DOLOR_INSTANCE = DOLOR;
	public static final Expression WELL_INSTANCE = WELL;
	
	public static Expression newExpression(String start, String end){
		return new SimpleExpression(start, end);
	}

	public static class Context {
		public static Context create(String str){
			Context ctx = new Context(str);
			ctx.varIndexMap = new HashMap<String, Integer>(5);
			return ctx;
		}
		@SuppressWarnings("unchecked")
		public static Context createWithEmptyVarIndex(String str){
			Context ctx = new Context(str);
			ctx.varIndexMap = Collections.EMPTY_MAP;
			return ctx;
		}
		Map<String, Integer> varIndexMap;
		String result;
		private Context(String result) {
			super();
			this.result = result;
		}
		public Integer getVarIndex(String var) {
			Integer index = this.varIndexMap.get(var);
			return index;
		}
	}

	protected String start;
	protected String end;
	protected boolean throwIfVarNotfound = false;
//	protected String text;
//	protected Map<String, Integer> varIndexMap = new LinkedHashMap<String, Integer>();

	protected Object valueProvider;
	public SimpleExpression(String start, String end, Object valueProvider) {
		this(start, end);
		this.valueProvider = valueProvider;
	}

	public SimpleExpression(String start, String end) {
		this.start = start;
		this.end = end;
	}

	public boolean isThrowIfVarNotfound() {
		return throwIfVarNotfound;
	}

	public void setThrowIfVarNotfound(boolean throwIfVarNotfound) {
		this.throwIfVarNotfound = throwIfVarNotfound;
	}

	public boolean isExpresstion(String text) {
		return StringUtils.isNotBlank(text) && text.indexOf(start) != -1 && text.indexOf(end) != -1;
	}

	/*public boolean isExpresstion() {
		return this.isExpresstion(this.text);
	}*/

	public String parse(String text) {
		return parseByProvider(text, this.valueProvider);
	}

	public String parse(String text, Object... objects) {
		return parseWithContext(text, objects).result;
	}
	

	public Context parseWithContext(String text, Object... objects) {
		if (!isExpresstion(text))
			return Context.createWithEmptyVarIndex(text);

		final Map context = MyUtils.convertParamMap(objects);
		Context ctx = parseWithContext(text, context);

		return ctx;
	}
	
	/*public String parse(ValueProvider provider) {
		return parse(this.text, provider);
	}*/

	public String parseByProvider(String text, Object provider){
		return parseWithContext(text, provider).result;
	}
	
	public Context parseWithContext(String text, Object provider) {
//		Assert.notNull(provider, "provider can not be null!");
		if(provider==null)
			return Context.createWithEmptyVarIndex(text);
		
		if (StringUtils.isBlank(text))
			return Context.createWithEmptyVarIndex("");

		int beginIndex = text.indexOf(start);
		if (beginIndex == -1)
			return Context.createWithEmptyVarIndex("");

//		varIndexMap.clear();
		Context context = Context.create("");
		
		String var = null;
		int varIndex = 1;
		StringBuilder sb = new StringBuilder();
		int off = 0;
		while (beginIndex != -1) {
			int endIndex = text.indexOf(end, beginIndex);
			if (endIndex == -1)
				break;

			sb.append(text.substring(off, beginIndex));

			var = text.substring(beginIndex + start.length(), endIndex);
			// sb.delete(beginIndex, endIndex+1);
			if (StringUtils.isBlank(var)){
				beginIndex = text.indexOf(start, endIndex);
				off = endIndex + end.length();
				continue;
			}
			String value = getValueByVar(provider, var);
			// sb.insert(beginIndex, value);
			if (value != null)
				sb.append(value);
			beginIndex = text.indexOf(start, endIndex);
			off = endIndex + end.length();

			context.varIndexMap.put(var, varIndex++);
		}
		if (off < text.length()) {
			sb.append(text.substring(off));
		}
		context.result = sb.toString();
		return context;
	}
	
	protected String getValueByVar(Object provider, String var){
		Assert.notNull(provider, "provider can not be null!");
		
		String result = null;
		if(provider instanceof ValueProvider)
			result = ((ValueProvider) provider).findString(var);
		else if(provider instanceof Map){
			Object val = ((Map)provider).get(var);
			result = LangUtils.toString(val);
		}else if(provider instanceof List){
			try {
				int index = Integer.parseInt(var);
				List list = (List)provider;
				if(index<list.size())
					result = LangUtils.toString(list.get(index));
			} catch (Exception e) {
				//ignore
			}
		}else if(provider.getClass().isArray()){
			try {
				int index = Integer.parseInt(var);
				Object[] arys = (Object[])provider;
				if(index<arys.length)
					result = LangUtils.toString(arys[index]);
			} catch (Exception e) {
				// ignore
			}
		}else if(!LangUtils.isBaseTypeObject(provider)){
			Object obj = ReflectUtils.getExpr(provider, var);
			result = (obj==null?"":obj.toString());
		}else{
			throw new IllegalArgumentException("provider must be a ValueProvider type : " + provider.getClass());
		}
		
		if(result==null)
			return throwVarNotfoundOrNullString(var);
		else
			return result;
	}
	
	protected String throwVarNotfoundOrNullString(String var){
		if(isThrowIfVarNotfound())
			LangUtils.throwBaseException("can not find the value of varname: " + var);
		return "NULL";
	}

	/*public Integer getVarIndex(String var) {
		Integer index = this.varIndexMap.get(var);
		return index;
	}*/

	/***************************************************************************
	 * 变量与位置的映射 如：文本"text${var1}test${var2}test"经过SimpleExpression解释后
	 * 通过此方法可返回下面的映射： var1 --> 1 var2 --> 2
	 * 
	 * @return
	 */
	/*public Map<String, Integer> getVarIndexMap() {
		return varIndexMap;
	}*/

	public static void main(String[] args) {
		String str = "ads-${bb}-asdf--${cc}";
		Expression sp = new SimpleExpression("${", "}");
		System.out.println(sp.isExpresstion(str));
		System.out.println("str: " + sp.parse(str, "bb", "test"));
		Context ctx = sp.parseWithContext(str, "bb", "test");
		System.out.println(ctx.getVarIndex("bb"));
	}

}
