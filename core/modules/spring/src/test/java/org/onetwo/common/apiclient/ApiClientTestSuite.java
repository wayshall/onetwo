package org.onetwo.common.apiclient;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.spring.rest.RestUtilsTest;

/**
 * @author wayshall
 * <br/>
 */

@RunWith(Suite.class)
@SuiteClasses({
	RestUtilsTest.class,
	WeatherClientTest.class,
	Ys7AccessTokenClientTest.class
})
public class ApiClientTestSuite {

}
