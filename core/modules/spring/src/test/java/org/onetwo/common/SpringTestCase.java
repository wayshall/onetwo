package org.onetwo.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.apiclient.ApiClientTestSuite;
import org.onetwo.common.spring.SpringUtilsTest;
import org.onetwo.common.spring.rest.RestUtilsTest;
import org.onetwo.common.spring.utils.MapToBeanConvertorTest;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	ApiClientTestSuite.class,
	RestUtilsTest.class,
	SpringUtilsTest.class,
	MapToBeanConvertorTest.class
})
public class SpringTestCase {

}

