package org.onetwo.boot.core.listener;
/**
 * @author weishao zeng
 * <br/>
 */

import org.onetwo.boot.utils.BootUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class BootApplicationReadyListener {
	
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady(ApplicationReadyEvent event) {
		BootUtils.sutdownAsyncInitor();
	}

}
