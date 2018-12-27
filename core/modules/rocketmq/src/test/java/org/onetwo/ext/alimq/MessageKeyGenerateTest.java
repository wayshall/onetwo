package org.onetwo.ext.alimq;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Date;

import org.junit.Test;
import org.onetwo.common.utils.LangOps;
import org.onetwo.dbm.id.SnowflakeIdGenerator;
import org.onetwo.ext.ons.ONSUtils;

public class MessageKeyGenerateTest {

	SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
	
	@Test
	public void testKey() {
		SimpleMessage event = new SimpleMessage();
		event.setDataId(""+idGenerator.nextId());
		event.setUserId(""+idGenerator.nextId());
		event.setOccurOn(new Date());
		String key = ONSUtils.toKey("EVENT", "act_published", event).getKey();
		System.out.println("keyp["+key.length()+"]:"+key);
//		System.out.println(Long.parseLong("1xodpywl2s5h", 36));
	}
	

	@Test
	public void testKeys() {
		LangOps.ntimesRun(100, ()->{
			testKey();
		});
	}
	

}

