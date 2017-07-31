package org.onetwo.boot.core.web.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.core.web.view.XResponseView.XResponseViews;
import org.onetwo.common.data.DataResultWrapper;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(XResponseViews.class)
public @interface XResponseView {
	
	String value();
	Class<? extends DataResultWrapper> wrapper();
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface XResponseViews {
		
		XResponseView[] value();
	}
}
