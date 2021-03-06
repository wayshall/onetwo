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
	
	String[] excludeEnumNames() default {"DELETE"};
	
	Class<? extends UISelectDataProvider> dataProvider() default NoProvider.class;

	Class<?> cascadeEntity() default Void.class;
	String[] cascadeQueryFields() default {};
	
	String labelField() default "label";
	String valueField() default "value";
	
	boolean treeSelect() default false;
	
//	String remoteUrl() default "/web-admin/dbm/uiselect/dataProvider";
	
	public enum NoEnums {
	}
	public class NoProvider implements UISelectDataProvider {
		@Override
		public List<?> findDatas(SelectQueryRequest query) {
			throw new UnsupportedOperationException();
		}
	}
}
