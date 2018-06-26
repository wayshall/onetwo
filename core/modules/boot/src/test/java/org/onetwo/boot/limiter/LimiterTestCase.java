package org.onetwo.boot.limiter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author wayshall
 * <br/>
 */
@RunWith(Suite.class)
@SuiteClasses({
	MatcherTest.class,
	LocalRateLimiterTest.class,
	LocalIntervalLimiterTest.class
})
public class LimiterTestCase {

}
