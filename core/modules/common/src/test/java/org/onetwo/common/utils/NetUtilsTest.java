package org.onetwo.common.utils;

import org.junit.Test;

public class NetUtilsTest {
	
	@Test
	public void testGetHostAddress(){
		String address = NetUtils.getHostAddress();
		System.out.println("address:"+address);
		address = NetUtils.getHostName();
		System.out.println("address:"+address);
		

		address = NetUtils.getLocalHostLANIp();
		System.out.println("getLocalHostLANIp:"+address);
	}
	
	

}
