package org.onetwo.plugins.rest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

public class RestRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

	private static class JFishServletRequestDataBinder extends ExtendedServletRequestDataBinder {

		private static final Pattern NESTED_PATH_PATTER = Pattern.compile("(\\[([a-z]+[\\w]+)\\])");

		public JFishServletRequestDataBinder(Object target, String objectName) {
			super(target, objectName);
		}

		/*private String translateName(String str) {
			String newPath = str;
			if (str.contains("[") && str.contains("]")) {
				Matcher m = NESTED_PATH_PATTER.matcher(str);
				if (m.find()) {
					newPath = newPath.replace(m.group(1), "." + m.group(2));
				}
			}
			return newPath;
		}*/
		

		/****
		 * 自定义AbstractPropertyBindingResult
		 * 从而自定义PropertyAccessorFactory
		 */
		protected AbstractPropertyBindingResult getInternalBindingResult() {
			/*if (this.bindingResult == null) {
				initBeanPropertyAccess();
			}
			return this.bindingResult;*/
			return super.getInternalBindingResult();
		}

		@Override
		protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
			String str = null;
			for (PropertyValue pv : mpvs.getPropertyValues()) {
				str = pv.getName();
				if (str.contains("[") && str.contains("]")) {
					Matcher m = NESTED_PATH_PATTER.matcher(str);
					boolean rs = m.find();
					if (rs) {
						String newPath = m.replaceAll(".$2");
						mpvs.removePropertyValue(str);
						mpvs.add(newPath, pv.getValue());
					}
				}
			}
			super.addBindValues(mpvs, request);
		}

	}

	private static class JFishRequestDataBinderFactory extends ServletRequestDataBinderFactory {

		public JFishRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
			super(binderMethods, initializer);
		}

		@Override
		protected ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request) {
			return new JFishServletRequestDataBinder(target, objectName);
		}

	}

	protected ServletRequestDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods) throws Exception {
		return new JFishRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
	}
	
}
