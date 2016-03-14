package org.onetwo.common.id;

import org.junit.Test;
import org.onetwo.common.utils.LangOps;

public class IdWorkerTest {
	
	@Test
	public void testId(){
		IdWorker idw = new IdWorker(1, 1);
		
		LangOps.repeatRun(100, ()->{
			System.out.println("id:"+idw.nextId());
		});
	}

}
