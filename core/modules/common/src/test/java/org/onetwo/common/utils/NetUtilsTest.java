package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NetUtilsTest {
	
	@Test
	public void testIsInternalIP(){
		boolean isInternal = NetUtils.isInternalIP("http://192.168.1.1");
		System.out.println("isInternal:"+isInternal);
		assertThat(isInternal).isTrue();
		
		isInternal = NetUtils.isInternalIP("https://192.168.1.1");
		System.out.println("isInternal:"+isInternal);
		assertThat(isInternal).isTrue();

		isInternal = NetUtils.isInternalIP("http://www.test.com");
		System.out.println("isInternal:"+isInternal);
		assertThat(isInternal).isFalse();

		isInternal = NetUtils.isInternalIP("aaa");
		System.out.println("isInternal:"+isInternal);
		assertThat(isInternal).isFalse();
	}
	
	
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
