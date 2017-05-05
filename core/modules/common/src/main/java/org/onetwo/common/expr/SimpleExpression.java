package org.onetwo.common.expr;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("rawtypes")
public class SimpleExpression implements Expression {
 
//	public static final Expression DOLOR_INSTANCE = DOLOR;
//	public static final Expression WELL_INSTANCE = WELL;
	

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

	private final static String NULL_VALUE = "NULL";
	
	private final String start;
	private final String end;
	private final boolean throwIfVarNotfound;
//	protected String text;
//	protected Map<String, Integer> varIndexMap = new LinkedHashMap<String, Integer>();
	protected final Object valueProvider;
	
	private final String nullValue;
	
	public SimpleExpression(String start, String end, Object valueProvider, String nullValue) {
		this.start = start;
		this.end = end;
		this.valueProvider = valueProvider;
		this.nullValue = nullValue;
		this.throwIfVarNotfound = ":throw".equalsIgnoreCase(nullValue);
	}
	

	public SimpleExpression(String start, String end, Object valueProvider) {
		this(start, end, valueProvider, NULL_VALUE);
	}

	public SimpleExpression(String start, String end) {
		this(start, end, null, NULL_VALUE);
	}

	public boolean isThrowIfVarNotfound() {
		return throwIfVarNotfound;
	}
/*
	public void setThrowIfVarNotfound(boolean throwIfVarNotfound) {
		this.throwIfVarNotfound = throwIfVarNotfound;
	}*/

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

		Context ctx = null;
		
		if(objects.length==1 && objects[0]!=null){
			ctx = parseWithContextByProvider(text, objects[0]);
		}else{
			final Map context = MyUtils.convertParamMap(objects);
			ctx = parseWithContextByProvider(text, context);
		}

		return ctx;
	}
	
	/*public String parse(ValueProvider provider) {
		return parse(this.text, provider);
	}*/

	public String parseByProvider(String text, Object provider){
		return parseWithContextByProvider(text, provider).result;
	}
	
	public Context parseWithContextByProvider(String text, Object values) {
//		Assert.notNull(provider, "provider can not be null!");
		if(values==null)
			return Context.createWithEmptyVarIndex(text);
		
		if (StringUtils.isBlank(text))
			return Context.createWithEmptyVarIndex("");

		int beginIndex = text.indexOf(start);
		if (beginIndex == -1)
			return Context.createWithEmptyVarIndex(text);

//		varIndexMap.clear();
		Context context = Context.create("");
		
		ValueProvider provider = getValueProvider(values);
		
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
	
	protected String getValueByVar(ValueProvider provider, String var){
		Assert.notNull(provider, "provider can not be null!");
		
		String result = provider.findString(var);
		/*if(provider instanceof ValueProvider)
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
		}*/
		
		if(result==null)
			return throwVarNotfoundOrNullString(var);
		else
			return result;
	}


	static class JFishPropertiesProvider implements ValueProvider {
		final private JFishProperties props;
		public JFishPropertiesProvider(JFishProperties props) {
			this.props = props;
		}
		@Override
		public String findString(String var) {
			String result = props.getProperty(var, "");
			return result;
		}
	}
	
	static class MapValueProvider implements ValueProvider {
		final private Map<String, Object> context;
		public MapValueProvider(Map<String, Object> context) {
			super();
			this.context = context;
		}
		@Override
		public String findString(String var) {
			String result = LangUtils.toString(context.get(var));
			return result;
		}
	}

	static class ListValueProvider implements ValueProvider {
		final private List<?> list;
		public ListValueProvider(List<?> list) {
			super();
			this.list = list;
		}
		@Override
		public String findString(String var) {
			String result = "";
			try {
				int index = Integer.parseInt(var);
				if(index<list.size())
					result = LangUtils.toString(list.get(index));
			} catch (Exception e) {
				//ignore
			}
			return result;
		}
	}
	
	protected ValueProvider getValueProvider(Object provider){
		Assert.notNull(provider, "provider can not be null!");
		
		if(provider instanceof ValueProvider){
			return (ValueProvider) provider;
		
		}else if(provider instanceof JFishProperties){
			return new JFishPropertiesProvider((JFishProperties)provider);
			
		}else if(provider instanceof Map){
			return new MapValueProvider((Map<String, Object>)provider);
			
		}else if(provider instanceof List){
			return new ListValueProvider((List)provider);
			
		}else if(provider.getClass().isArray()){
			return new ListValueProvider(Arrays.asList((Object[])provider));
			
		}else if(!LangUtils.isBaseTypeObject(provider)){
			return new ValueProvider() {
				@Override
				public String findString(String var) {
					Object obj = ReflectUtils.getExpr(provider, var);
					String result = (obj==null?"":obj.toString());
					return result;
				}
			};
		}else{
			throw new IllegalArgumentException("provider must be a ValueProvider type : " + provider.getClass());
		}
		
	}
	
	protected String throwVarNotfoundOrNullString(String var){
		if(isThrowIfVarNotfound())
			LangUtils.throwBaseException("can not find the value of varname: " + var);
		return nullValue;
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
