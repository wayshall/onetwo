package org.onetwo.common.spring.validator.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.onetwo.common.spring.validator.MobileConstraintValidator;


/****
 * @author way
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileConstraintValidator.class)
@Documented
public @interface Mobile {
	String message() default "{constraints.Mobile}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /****
     * 正则表达式
     * 默认为简单的正则表达式，
     * @author weishao zeng
     * @return
     */
    String value() default "^1[3-9]\\d{9}$";
    
}
