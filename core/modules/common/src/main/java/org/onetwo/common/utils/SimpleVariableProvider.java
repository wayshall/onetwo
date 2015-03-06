package org.onetwo.common.utils;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;


/*********
 * VariableSupporter到ValuProvider的适配器
 * @author way
 *
 */
public class SimpleVariableProvider implements ValueProvider {
	
	protected Logger logger = JFishLoggerFactory.getLogger(SimpleVariableProvider.class);
	
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
