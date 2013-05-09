package org.onetwo.common.utils;

import org.apache.log4j.Logger;

/*********
 * VariableSupporter到ValuProvider的适配器
 * @author way
 *
 */
public class SimpleVariableProvider implements ValueProvider {
	
	protected Logger logger = Logger.getLogger(SimpleVariableProvider.class);
	
	protected Object context;

	
	public SimpleVariableProvider(Object context){
		this.context = context;
	}
	
	@Override
	public String findString(String var) {
		Object val = MyUtils.getValue(context, var);
		return val==null?null:val.toString();
	}

}
