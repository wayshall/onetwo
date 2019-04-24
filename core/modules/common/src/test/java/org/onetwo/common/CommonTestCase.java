package org.onetwo.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.reflect.BeanToMapConvertorTest;
import org.onetwo.common.reflect.IntroTest;
import org.onetwo.common.utils.DateUtilTest;
import org.onetwo.common.utils.LangUtilsTest;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	LangUtilsTest.class,
	DateUtilTest.class,
	IntroTest.class,
	BeanToMapConvertorTest.class
})
public class CommonTestCase {

}

