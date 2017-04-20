package org.onetwo.common;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;



public class SimpleTest {

	private static volatile boolean stop = false;
	
	public static void main(String[] args) {
//		LangUtils.CONSOLE.exitIf("exit");
		while(!stop){
			try {
				System.out.println("wait");
				SimpleTest.class.wait();
			} catch (Throwable e) {
				System.out.println(e.getMessage());
			}
		}
    }
	

}
