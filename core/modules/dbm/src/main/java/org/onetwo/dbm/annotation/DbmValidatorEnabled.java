package org.onetwo.dbm.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/******
 * 可定义在实体类上，标记是否激活验证器
 * @author way
 *
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmValidatorEnabled {
}
