package org.onetwo.common.spring.underline;

import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;

@SuppressWarnings("serial")
public class UnderlineBeanPropertyBindingResult extends BeanPropertyBindingResult {

	public UnderlineBeanPropertyBindingResult(Object target, String objectName,
			boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
		super(target, objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
	}

	public UnderlineBeanPropertyBindingResult(Object target, String objectName) {
		super(target, objectName);
	}

	protected BeanWrapper createBeanWrapper() {
		Assert.state(this.getTarget() != null, "Cannot access properties on null bean instance '" + getObjectName() + "'!");
		return new UnderlineBeanWrapper(this.getTarget());
	}
	
}
