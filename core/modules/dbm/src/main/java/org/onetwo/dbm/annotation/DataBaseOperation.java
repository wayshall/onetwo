package org.onetwo.dbm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*****
 * 标记session操作db相关方法
 * @author way
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBaseOperation {
	
	OperationType type();
	
	enum OperationType {
		QUERY,
		UPDATE,
		INSERT,
		SAVE,
		DELETE,
		EXECUTE,
		BATCH_INSERT,
		BATCH_UPDATE,
	}
}
