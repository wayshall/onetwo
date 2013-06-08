package org.onetwo.sizeof;

import net.sourceforge.sizeof.SizeOf;

import org.junit.Test;

public class SizeOfTest {

	@Test
	public void testSizeOf(){
		SizeOf.skipStaticField(true); //java.sizeOf will not compute static fields
		SizeOf.skipFinalField(true); //java.sizeOf will not compute final fields
		SizeOf.skipFlyweightObject(true); //java.sizeOf will not compute well-known flyweight objects
		Object a = new Object();
		System.out.println(SizeOf.deepSizeOf(a)); //this will print the object size in bytes
	}
}
