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

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import org.onetwo.common.spring.validator.IDCardNoConstraintValidator;
import org.onetwo.common.utils.LangUtils;


/****
 * @author way
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDCardNoConstraintValidator.class)
@Documented
public @interface IDCardNo {
	String message() default "{constraints.IDCardNo}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /****
     * 正则表达式
     * 默认为简单的正则表达式，
     * @author weishao zeng
     * @return
     */
    String value() default LangUtils.IDCARD_PATTERN;
    
}
