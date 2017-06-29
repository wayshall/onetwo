package org.onetwo.common.spring.convert;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.onetwo.common.reflect.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
public class GenericEnumValueTest {
	
	@Test
	public void testGenericValue(){
		Class<?> genericClass = TypeResolver.resolveRawArgument(GenericValueEnum.class, TestEnum.class);
		assertThat(genericClass).isEqualTo(String.class);
	}
	public interface GenericValueEnum<T> {
		public T getValue();
	}
	public static enum TestEnum implements GenericValueEnum<String> {
		TEST1,
		TEST2
		;

		@Override
		public String getValue() {
			return name()+"Value";
		}
		
	}
}
