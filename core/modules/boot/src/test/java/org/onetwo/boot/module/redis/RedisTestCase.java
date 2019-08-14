package org.onetwo.boot.module.redis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author weishao zeng
 * <br/>
 */

@RunWith(Suite.class)
@SuiteClasses({
	JsonRedisTemplateTest.class,
	RedisOperationServiceTest.class,
	TokenValidatorTest.class
})
public class RedisTestCase {

}

