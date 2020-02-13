package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUIInput {
	
	InputTypes type() default InputTypes.TEXT;
	
	Class<? extends DUIJsonValueWriter<?>> valueWriter() default NullDUIJsonValueWriter.class;
	
	public enum InputTypes {
		TEXT,
		TEXT_AREA,
		FILE,
		NUMBER,
		DATE,
		TIME,
		DATE_TIME,
		SWITCH
	}
}
