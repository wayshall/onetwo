package org.onetwo.common.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class JFishBeanWrapper extends BeanWrapperImpl {

	final Pattern NESTED_PATH_PATTERN = Pattern.compile("(\\[([a-z]+\\w)\\])");

	public JFishBeanWrapper(Class<?> clazz) {
		super(clazz);
	}

	public JFishBeanWrapper(Object object) {
		super(object);
	}

	@Override
	public void setPropertyValue(String propertyName, Object value) throws BeansException{
		String newPath = translatePropertyPath(propertyName);
		super.setPropertyValue(newPath, value);
	}
	
	protected String translatePropertyPath(String propertyPath) {
		String newPath = propertyPath;
		if(propertyPath.contains("[") && propertyPath.contains("]")){
			Matcher matcher = NESTED_PATH_PATTERN.matcher(newPath);
			if(matcher.find()){
				String s = matcher.group(1);
				String r = "."+matcher.group(2);
				newPath = newPath.replace(s, r);
			}
		}
		return newPath;
	}
	
}
