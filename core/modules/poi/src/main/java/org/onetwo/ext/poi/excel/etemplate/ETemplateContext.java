package org.onetwo.ext.poi.excel.etemplate;

import java.util.Collections;
import java.util.Map;

import org.onetwo.ext.poi.utils.TheFunction;

import com.google.common.collect.Maps;

public class ETemplateContext {
	
	public static final ETemplateContext newContext() {
		return new ETemplateContext();
	}
	
	private Map<String, Object> rootObject = Maps.newHashMap();
	private Map<String, Object> dataContext = Maps.newHashMap();
	/***
	 * 是否在输出前执行公式
	 */
	private boolean evaluateFormula = true;
//	private Object rootObject;
	
	public ETemplateContext(){
		dataContext.put(TheFunction.ALIAS_NAME, TheFunction.getInstance());
	}

	public Object put(String key, Object value) {
		return rootObject.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		rootObject.putAll(m);
	}
	

	public Object remove(Object key) {
		return rootObject.remove(key);
	}

	public Object get(Object key) {
		return rootObject.get(key);
	}

	public Map<String, Object> getRootObject() {
//		return ImmutableMap.copyOf(rootContext);
		return Collections.unmodifiableMap(rootObject);
	}

	public Map<String, Object> getDataContext() {
		return dataContext;
	}
	
	public boolean isEvaluateFormula() {
		return evaluateFormula;
	}

	public void setEvaluateFormula(boolean evaluateFormula) {
		this.evaluateFormula = evaluateFormula;
	}

	public ETemplateContext clone() {
		ETemplateContext ctx = new ETemplateContext();
		ctx.rootObject.putAll(rootObject);
		ctx.dataContext.putAll(dataContext);
		ctx.evaluateFormula = evaluateFormula;
		return ctx;
	}
	
	

	/*public Object getRootObject() {
		return rootObject;
	}

	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}
	*/

}
