package org.onetwo.boot.module.redis;

import org.junit.Test;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;



/**
 * @author wayshall
 * <br/>
 */
public class ThreadLocalRandomTest {
	
	@Test
	public void test(){
		LangOps.ntimesRun("ThreadLocalRandom", 100, ()->{
			String value = LangUtils.getRadomNumberString(5);
			System.out.println(value);
		});
		LangOps.ntimesRun("getRadomString", 100, ()->{
			System.out.println("str:"+LangUtils.getRadomString(5));
		});
	}

}
