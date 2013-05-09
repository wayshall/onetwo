package org.onetwo.common.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class MyClassLoader extends URLClassLoader {


	public MyClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}


	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class c = null;
		try {
			String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
			System.out.println(getClass().getClassLoader() + " : " +getClass() + " : " + name);
			InputStream ins = getClass().getResourceAsStream(fileName);
			if (ins == null) {
				Class clz = super.loadClass(name);
				System.out.println("super: " + clz.getClass().getClassLoader());
				return clz;
			}
			byte[] b = new byte[ins.available()];
			ins.read(b);
			c = defineClass(name, b, 0, b.length);
			resolveClass(c);
			return c;
		} catch (IOException e) {
			e.printStackTrace();
		}
		c = super.loadClass(name);
		return c;
	}
}
