package org.onetwo.common.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class JFishBeanWrapper extends BeanWrapperImpl {

	/***
	 * 查找中括号形式的属性
	 */
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
			// 查找中括号形式的属性，比如bean[name]
			Matcher matcher = NESTED_PATH_PATTERN.matcher(newPath);
			if(matcher.find()){
				// 查找中括号属性：[name]
				String s = matcher.group(1);
				// 转换为dot属性：.name
				String r = "."+matcher.group(2);
				// bean[name] => bean.name
				newPath = newPath.replace(s, r);
			}
		}
		return newPath;
	}
	
}
