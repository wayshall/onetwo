package org.onetwo.common.delegate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.delegate.DelegateFactory;
import org.onetwo.common.utils.delegate.MutiDelegate;

public class DelegateTest {

	public static class PrintTest {
		public void print(String msg, int count){
			for(int i=0; i<count; i++){
				System.out.println("print count["+i+"] : " + msg);
			}
		}
	}
	
	public static class CreateStringTest {
		public List<String> createStringList(String e, int count){
			List<String> result = new ArrayList<String>();
			for(int i=0; i<count; i++){
				result.add(e);
			}
			return result;
		}
	}
	
	@Test
	public void testOne(){
		MutiDelegate delegate = DelegateFactory.define(String.class, int.class);
		PrintTest pt = new PrintTest();
		CreateStringTest cst = new CreateStringTest();
		
		delegate.add(pt, "print");
		delegate.add(cst, "createStringList");
		
		List<?> results = delegate.invokeAsList("test", 3);
		Assert.assertNull(LangUtils.getFirst(results));
		Assert.assertEquals(3, ((List<?>)results.get(1)).size());
	}
}
