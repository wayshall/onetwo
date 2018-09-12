package org.onetwo.boot.core.web.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.view.XResponseView.XResponseViews;
import org.onetwo.common.data.DataResultWrapper;

/**
 * 根据header[XResponseView]指定经过DataResultWrapper包装
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(XResponseViews.class)
@Inherited
public @interface XResponseView {
	String DEFAULT_VIEW = "default";
	
	String value() default DEFAULT_VIEW;
	Class<? extends DataResultWrapper> wrapper() default DefaultDataResultWrapper.class;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	@Inherited
	public @interface XResponseViews {
		
		XResponseView[] value();
	}
}
