package org.onetwo.common.utils.propconf;

import java.text.MessageFormat;
import java.util.Properties;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.convert.Types;

public class SimpleProperties {
	
	private final Properties properties;
	private final Expression parser = ExpressionFacotry.DOLOR;
	
	public SimpleProperties(String fileName){
		properties = PropUtils.loadProperties(fileName);
	}
	
	public SimpleProperties(Properties properties){
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public <T> T getPropertyAsType(String key, Class<T> type){
		String val = getProperty(key, "");
		return Types.asValue(val, type);
	}

	public String getProperty(String key, Object...args){
		return getPropertyWithDefault(key, null, args);
	}
	
	public String getPropertyWithDefault(String key, String defValue, Object...args){
		String val;
		if(!properties.containsKey(key)){
			if(defValue!=null){
				val = defValue;
				return formatMessage(val, args);
			}else{
				throw new ServiceException("can not find the key["+key+"] in properties.");
			}
		}
		val = properties.getProperty(key, defValue);
		val = formatMessage(val, args);
		return val;
	}
	
	private String formatMessage(String pattern, Object...args){
		if(LangUtils.isEmpty(args))
			return pattern;
		String msg = MessageFormat.format(pattern, args);
		return msg;
	}

	public boolean containsKey(Object key) {
		return properties.containsKey(key);
	}

	
	private String parseNamedMessage(String text, Object...args){
		if(LangUtils.isEmpty(args))
			return text;
		String msg = parser.parse(text, args);
		return msg;
	}
	

	public String parseNamed(String key, Object...args){
		return parseNamedWithDefault(key, null, args);
	}
	
	public String parseNamedWithDefault(String key, String defValue, Object...args){
		String val;
		if(!properties.containsKey(key)){
			if(defValue!=null){
				val = defValue;
				return parseNamedMessage(val, args);
			}else{
				throw new ServiceException("can not find the key["+key+"] in properties.");
			}
		}
		val = properties.getProperty(key, defValue);
		val = parseNamedMessage(val, args);
		return val;
	}

	
}
