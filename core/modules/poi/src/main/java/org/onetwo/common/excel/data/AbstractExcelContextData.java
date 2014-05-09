package org.onetwo.common.excel.data;

import java.util.Map;

abstract public class AbstractExcelContextData implements ContextParser {
	

	public AbstractExcelContextData(){
	}
	
	abstract protected AbstractExcelContextData getParentContextData();
	
	
	/*public AbstractExcelContextData(boolean parentContext, Map<String, Object> context){
//		Assert.notNull(context);
		Map<String, Object> selfContext;
		if(parentContext){
			selfContext = Maps.newHashMap();
			selfContext.putAll(context);
		}else{
			selfContext = context;
		}
		this.selfContext = selfContext;
	}
	
	
	protected void setSelfContext(Map<String, Object> selfContext) {
		this.selfContext = selfContext;
	}*/

	abstract public Map<String, Object> getSelfContext();
	protected Object getRootObject() {
		return null;
	}

	@Override
	public Object parseValue(String expr) {
		return getExcelValueParser().parseValue(expr, getRootObject(), getSelfContext());
	}
	

	public int parseIntValue(String expr) {
		return getExcelValueParser().parseIntValue(expr, getRootObject());
	}
	
	/*@Override
	public Map<String, Object> getContext(ConxtScope scope) {
		Map<String, Object> context = null;
		switch (scope) {
			case WORKBOOK:
				context = getWorkbookContext();
				break;
				
			case SHEET:
				context = getWorkbookContext();
				break;
	
			default:
				break;
			}
		return context;
	}*/
	
	abstract protected ExcelValueParser getExcelValueParser();
	
}
