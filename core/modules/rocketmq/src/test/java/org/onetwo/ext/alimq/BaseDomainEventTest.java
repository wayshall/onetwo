package org.onetwo.ext.alimq;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Date;

import org.junit.Test;
import org.onetwo.common.utils.LangOps;
import org.onetwo.dbm.id.SnowflakeIdGenerator;
import org.onetwo.ext.alimq.BaseDomainEvent.DomainEvent;

public class BaseDomainEventTest {

	SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
	
	@Test
	public void testKey() {
		BaseDomainEvent event = new BaseDomainEvent();
		event.setEvent(TestDomainEventTypes.ORDER_CREATED);
		event.setDataId(""+idGenerator.nextId());
//		event.setUserId(""+idGenerator.nextId());
		event.setOccurOn(new Date());
		String key = event.toKey();
		System.out.println("keyp["+key.length()+"]:"+key);
	}
	

	@Test
	public void testKeys() {
		LangOps.ntimesRun(100, ()->{
			testKey();
		});
	}
	
	public static enum TestDomainEventTypes implements DomainEvent {
		ORDER_CREATED,
		ORDER_UPDATED;

		@Override
		public String getName() {
			return name();
		}
		
	}

}

