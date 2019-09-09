package org.onetwo.common.jackson;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author weishao zeng
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	JsonMapperTest.class,
	JacksonXmlMapperTest.class,
	JsonMapperParameterizedTest.class,
	JsonMapperReadTreeTest.class
})
public class JacksonTestCase {

}

