package org.onetwo.sizeof;

import net.sourceforge.sizeof.SizeOf;

import org.junit.Test;

/***
 * -javaagent:D:/mydev/workspace/onetwo/labs/test-onetwo/lib/SizeOf.jar
 * @author Administrator
 *
 */
public class SizeOfTest {

	@Test
	public void testSizeOf(){
		SizeOf.skipStaticField(true); //java.sizeOf will not compute static fields
		SizeOf.skipFinalField(true); //java.sizeOf will not compute final fields
		SizeOf.skipFlyweightObject(true); //java.sizeOf will not compute well-known flyweight objects
		SizeOf.turnOnDebug();
		
//		SizeOf.setMinSizeToLog(1024); //min object size to log in bytes
//		SizeOf.setLogOutputStream(new FileOutputStream("<your log file>"));
		 
		System.out.println(SizeOf.deepSizeOf(new Object())); //this will print the object size in bytes
		System.out.println(SizeOf.deepSizeOf(new Integer(0))); 
		System.out.println(SizeOf.deepSizeOf(new String(""))); 
	}
}
