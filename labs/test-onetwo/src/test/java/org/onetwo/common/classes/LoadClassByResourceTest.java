package org.onetwo.common.classes;

import java.net.URL;

public class LoadClassByResourceTest {

	public static void main(String[] args) throws Exception {
		//可用于排除jar包冲突
		URL url = LoadClassByResourceTest.class.getResource("LoadClassByResourceTest.class");
		System.out.println(url);
	}
}
