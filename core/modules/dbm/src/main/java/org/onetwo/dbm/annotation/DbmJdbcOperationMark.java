package org.onetwo.dbm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.dbm.core.DbmJdbcOperationType;

/*****
 * 标记session操作db相关方法
 * @author way
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmJdbcOperationMark {
	
	DbmJdbcOperationType type();
	

}
