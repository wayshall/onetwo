package org.onetwo.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

import redis.embedded.RedisServer;

public class SomeIntegrationTestThatRequiresRedisTest {
	 private RedisServer redisServer;

	  @Before
	  public void setup() throws Exception {
	    redisServer = new RedisServer(6379); // or new RedisServer("/path/to/your/redis", 6379);
	    redisServer.start();
	    LangUtils.CONSOLE.exitIf("exit");
	  }

	  @Test
	  public void test() throws Exception {
	    // testing code that requires redis running
	  }

	  @After
	  public void tearDown() throws Exception {
	    redisServer.stop();
	  }
}
