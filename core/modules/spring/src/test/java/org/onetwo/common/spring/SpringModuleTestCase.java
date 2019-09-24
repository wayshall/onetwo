package org.onetwo.common.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.spring.rest.RestUtilsTest;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	RestUtilsTest.class,
	SpringUtilsTest.class
})
public class SpringModuleTestCase {

}

