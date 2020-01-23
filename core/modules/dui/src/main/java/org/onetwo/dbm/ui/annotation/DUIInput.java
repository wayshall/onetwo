package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUIInput {
	
	InputTypes type() default InputTypes.TEXT;
	
	Class<? extends DUIJsonValueWriter> valueWriter() default NullDUIJsonValueWriter.class;
	
	final public class NullDUIJsonValueWriter implements DUIJsonValueWriter {

		@Override
		public void write(Object value, DUIFieldMeta field, JsonGenerator jgen) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public enum InputTypes {
		TEXT,
		TEXTAREA
	}
}
