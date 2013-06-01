package org.onetwo.common.classloader;

import java.io.File;
import java.net.URL;



public class ClassLoaderTest {
	public static void main(String[] args) throws Exception {
		String path = "E:/mydev/ejb3/lvyou2/onetwo-common/test";
		URL url = new File(path).toURI().toURL();
		URL[] urls = new URL[]{url};
		ClassLoader myLoader = new MyClassLoader(urls, ClassLoaderTest.class.getClassLoader()) ;
//		ClassLoader myLoader2 = new MyClassLoader() ;
		Class userClass = myLoader.loadClass("org.onetwo.common.utils.User");
		System.out.println("userClass: " + userClass + ", laoder: " + userClass.getClassLoader());
		Thread.sleep(1000);
		System.out.println("==================================");
		Object obj3 = myLoader.loadClass("org.onetwo.common.utils.User").newInstance();
//		Object obj2 = myLoader2.loadClass("org.onetwo.common.utils.ClassLoaderTest").newInstance();
		/*System.out.println(obj.getClass());
		System.out.println(obj instanceof ClassLoaderTest);
		
		ClassLoaderTest a = new ClassLoaderTest();
		System.out.println(a instanceof ClassLoaderTest);*/
	}
}
