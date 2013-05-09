package org.onetwo.common.web.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ValueProvider;

import com.opensymphony.xwork2.ActionContext;

public class ValueProviderFactory{
	
	private static final Logger logger = Logger.getLogger(ValueProviderFactory.class);

	public static class StrutsContextProvider implements ValueProvider {
		@Override
		public String findString(String var) {
			String result = "";
			try {
				result = ActionContext.getContext().getValueStack().findString(var);
			} catch (Exception e) {
				logger.error(e);
			}
			return result;
		}
	}
	
	private static final Map<String, ValueProvider> PROVIDERS = new HashMap<String, ValueProvider>();
	
	static{
		PROVIDERS.put("struts", new StrutsContextProvider());
	}
	
	public static ValueProvider getStrutsValueProvider(){
		return getValueProvider("struts");
	}
	
	public static ValueProvider getValueProvider(String name){
		ValueProvider p = PROVIDERS.get(name);
		if(p==null)
			throw new ServiceException("no value provider named:"+name);
		return p;
	}
	

}
