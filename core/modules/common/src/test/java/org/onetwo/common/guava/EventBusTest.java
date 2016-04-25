package org.onetwo.common.guava;

import org.junit.Test;
import org.onetwo.common.exception.BaseException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class EventBusTest {
	public static class TestEvent{
		private String name;

		public TestEvent(String name) {
			super();
			this.name = name;
		}
		
	}
	@Test
	public void testEventBus(){
		EventBus eventBus = new EventBus();
		eventBus.register(new Object(){
			
			@Subscribe
			public void listener(TestEvent event){
				System.out.println("listener1:"+event.name);
			}
		});
		/*eventBus.register(new Object(){
			
			@Subscribe
			public void listener(TestEvent event){
				System.out.println("listener2:"+event.name);
				if(true)
					throw new BaseException("error");
			}
		});
		eventBus.register(new Object(){
			
			@Subscribe
			public void listener(TestEvent event){
				System.out.println("listener3:"+event.name);
				if(true)
					throw new BaseException("error");
			}
		});*/

		eventBus.post(new TestEvent("test"));
		eventBus.post(new TestEvent("test"));
	}

}
