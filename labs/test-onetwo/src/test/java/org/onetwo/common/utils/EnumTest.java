package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class EnumTest {
	public static enum StateType {
		WAITING(0, "等待"),
		EXECUTING(1, "正在执行"),
		SUCCEED(50, "成功"),
		FAILED(-50, "失败"),
		REPEATED(-10, "等待");

		private final int value;
		private final String name;

		private StateType(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
		
		public static StateType valueOf(Integer value){
			if(value==WAITING.value)
				return WAITING;
			else if(value==EXECUTING.value)
				return EXECUTING;
			return null;
		}
		
	}
	
	@Test
	public void testEnum(){
		Assert.assertEquals(0, StateType.WAITING.ordinal());
		Assert.assertEquals(1, StateType.EXECUTING.ordinal());
		Object val = ReflectUtils.getExpr(StateType.WAITING, "value");
		Assert.assertEquals(StateType.WAITING.getValue(), val);
		Object enu = ReflectUtils.invokeMethod("valueOf", StateType.class, val);
		Assert.assertEquals(StateType.WAITING, enu);
	}
	
	@Test
	public void testNumber(){
		float a = 0.4f;
		float b = 0.3f;
		float c = 0.1f;
		System.out.println("a: " + a);
		System.out.println("b: " + b);
		System.out.println("c: " + c);
		System.out.println("float: " + (a-b));
		Assert.assertFalse(c==(a-b));
	}
	
	@Test
	public void testWrap(){
		Integer i = new Integer("1000000");
		int j = 1000000;
		Integer n = null;
		Assert.assertTrue(i.equals(j));
		Assert.assertTrue(j==i);
		Assert.assertTrue(i==j);
//		Assert.assertTrue(j!=n);
	}
	
	
}
