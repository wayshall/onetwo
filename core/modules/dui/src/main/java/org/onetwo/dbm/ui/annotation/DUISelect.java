package org.onetwo.dbm.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.onetwo.dbm.ui.core.UISelectDataProvider;

/**
 * @author weishao zeng
 * <br/>
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DUISelect {
	
	Class<? extends Enum<?>> dataEnumClass() default NoEnums.class;
	
	Class<? extends UISelectDataProvider> dataProvider() default NoProvider.class;

	Class<?> cascadeEntity() default Void.class;
	String[] cascadeQueryFields() default {};
	
	String labelField() default "label";
	String valueField() default "value";
	
//	String remoteUrl() default "/web-admin/dbm/uiselect/dataProvider";
	
	public enum NoEnums {
	}
	public class NoProvider implements UISelectDataProvider {
		@Override
		public List<?> findDatas(String query) {
			throw new UnsupportedOperationException();
		}
	}
}
