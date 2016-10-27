package org.onetwo.common.spring.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/****
 * http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
 * @author way
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnyOneNotBlankConstraintValidator.class)
@Documented
public @interface AnyOneNotBlank {
//	String message() default "any one of the fields[#1] must can not be blank!";
	String message() default "{constraints.AnyOneNotBlank}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] value();
    
}
