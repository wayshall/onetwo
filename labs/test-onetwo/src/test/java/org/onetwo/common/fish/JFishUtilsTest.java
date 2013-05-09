package org.onetwo.common.fish;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.TestController;
import org.onetwo.common.profiling.UtilTimerStack;

public class JFishUtilsTest {
	
	@Test
	public void test(){
		UtilTimerStack.setActive(true);
		UtilTimerStack.push("test");
		String path = JFishUtils.getControllerPath(TestController.class, "test1");
		UtilTimerStack.pop("test");
		Assert.assertEquals("/test/test1", path);
		
		path = JFishUtils.getControllerPath(TestController.class, "test2");
		Assert.assertEquals("/test/test2", path);
		
		path = JFishUtils.getControllerPath(TestController.class, "test2", "pname1", "pvalue1");
		Assert.assertEquals("/test/test2?pname1=pvalue1", path);

		
		path = JFishUtils.getControllerPath(TestController.class, "test3");
		Assert.assertEquals("/test", path);
	}

}
