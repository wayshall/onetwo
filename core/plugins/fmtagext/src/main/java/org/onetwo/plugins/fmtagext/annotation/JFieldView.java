package org.onetwo.plugins.fmtagext.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFieldView {

	String label() default "";
	String formName() default "";
	String formValue() default "";
	String showValue() default "";
	String format() default "";
	int order() default Integer.MAX_VALUE;
	org.onetwo.plugins.fmtag.JFieldShowable[] showable() default {org.onetwo.plugins.fmtag.JFieldShowable.grid, org.onetwo.plugins.fmtag.JFieldShowable.create, org.onetwo.plugins.fmtag.JFieldShowable.update, org.onetwo.plugins.fmtag.JFieldShowable.show};
	org.onetwo.plugins.fmtagext.uitils.UIType formui() default org.onetwo.plugins.fmtagext.uitils.UIType.FORM_TEXT_INPUT;
}
