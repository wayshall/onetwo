package org.onetwo.common.web.utils;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.VariableSupporterProvider;
import org.onetwo.common.utils.propconf.VariableSupporter;

import com.opensymphony.xwork2.ActionContext;

public class WebVariableSupporterProvider extends VariableSupporterProvider {

	
	public WebVariableSupporterProvider(){
	}
	
	public WebVariableSupporterProvider(VariableSupporter variable){
		super(variable);
	}
	
	@Override
	public String findString(String var) {
		String val = variable.getVariable(var);
		if(val!=null){
			try {
				if(!var.startsWith("#"))
					val = (String)Ognl.getValue(var, variable);
			} catch (OgnlException oe) {
				logger.error(oe);
			}
			try {
				if(StringUtils.isBlank(val) && ActionContext.getContext()!=null){
					String temp = (String)ActionContext.getContext().getValueStack().findString(var);
					if(temp!=null)
						val = temp;
				}
			} catch (Exception e) {
				logger.error("can't find the value: " + var, e);
			}
		}
		return val;
	}
}
