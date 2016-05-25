package org.onetwo.common.expr;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.VariableSupporter;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

/*********
 * VariableSupporter到ValuProvider的适配器
 * @author way
 *
 */
public class VariableSupporterProvider implements ValueProvider {
	
	protected Logger logger = JFishLoggerFactory.getLogger(VariableSupporterProvider.class);
	
	protected VariableSupporter variable;

	public VariableSupporterProvider(){
	}
	
	public VariableSupporterProvider(VariableSupporter variable){
		this.variable = variable;
	}
	
	@Override
	public String findString(String var) {
		String val = variable.getVariable(var);
		if(StringUtils.isBlank(val)){
			try {
				//发现ongl性能非常一般，还是首先尝试通过反射来获取吧～
				Object v = ReflectUtils.getPropertyValue(variable, var);
				if(v!=null)
					val = v.toString();
				else
					val = "";
			} catch (Exception e) {
				val = "";
			}
			/*try {
				val = (String)Ognl.getValue(var, variable);
			} catch (Exception oe) {
				logger.error("can't find the value: " + var, oe);
			}*/
		}
		return val;
	}

	public void setVariable(VariableSupporter variable) {
		this.variable = variable;
	}
}
