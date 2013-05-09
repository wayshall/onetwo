package org.onetwo.plugins.fmtagext.ui.valuer;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.ValueProvider;
import org.onetwo.plugins.fmtagext.ui.FmUIComponent;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;


abstract public class AbstractUIValuer<T> implements UIValuer<T> {
	
	public AbstractUIValuer() {
	}

	abstract public String getValue();
	
	protected boolean isProperty(){
		return FmUIComponent.isProperty(getValue());
	}
	
	protected String asProperty(){
		return FmUIComponent.trimPropertyMark(getValue());
	}
	
	public UIValueProvider createUIValueProvider(Object viewObj){
		return new DefaultUIValueProvider(viewObj, "");
	}


	@SuppressWarnings("unchecked")
	@Override
	public T getUIValue(Object viewValue) {
		String value = getValue();
		if(StringUtils.isBlank(value) || viewValue==null){
			return null;
		}
		
		if(isProperty()){
			String propertyName = FmUIComponent.trimPropertyMark(value);
			return (T)createUIValueProvider(viewValue).getValue(propertyName);
			
		}else{
			return null;
		}
	}

	public static class MutableUIValuer extends AbstractUIValuer<Object> implements ExprUIValuer<Object>{

		private String value;
		
		private MutableUIValuer(String value) {
			super();
			this.value = value;
		}
		
		@Override
		public String getValue() {
			return value;
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}

	}
	
	public static class ImmutableUIValuer extends AbstractUIValuer<Object> {
		
		private final String value;

		public ImmutableUIValuer(String value) {
			super();
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
		
	}
	
	public static class DefaultUIValueProvider implements ValueProvider, UIValueProvider {

		private final Object valueProvider;
		private final String format;
		private BeanWrapper bw;
		
		public DefaultUIValueProvider(Object valueProvider, String format) {
			super();
			this.valueProvider = valueProvider;
			this.format = format;
		}
		
		/* (non-Javadoc)
		 * @see org.onetwo.plugins.fmtagext.ui.valuer.UIValueProvider#getValue(java.lang.String)
		 */
		@Override
		public Object getValue(String expr){
			if("this".equals(expr)){
				return valueProvider;
			}else if(expr.contains(".")){
				if(bw==null){
					bw = PropertyAccessorFactory.forBeanPropertyAccess(valueProvider);
					bw.setAutoGrowNestedPaths(true);
				}
				return bw.getPropertyValue(expr);
			}else{
				return ReflectUtils.getProperty(valueProvider, expr);
			}
		}

		/* (non-Javadoc)
		 * @see org.onetwo.plugins.fmtagext.ui.valuer.UIValueProvider#findString(java.lang.String)
		 */
		@Override
		public String findString(String var) {
			Object result = null;
			if(valueProvider!=null && !LangUtils.isBaseTypeObject(valueProvider)){
				Object actualValue = getValue(var);
				if(actualValue instanceof Date){
					actualValue = DateUtil.format(format, (Date)actualValue);
				}else if(actualValue instanceof Number && StringUtils.isNotBlank(format)) {
					NumberFormat nf = new DecimalFormat(format);
					nf.setRoundingMode(RoundingMode.HALF_UP);
					actualValue = nf.format(actualValue);
				}
				result = actualValue;
				
			}
			if(result==null){
				result = JFishWebUtils.webHelper().getWebRequestContext().get(var);
			}
			return result==null?"":result.toString();
		}
		
	}
	
	
}
