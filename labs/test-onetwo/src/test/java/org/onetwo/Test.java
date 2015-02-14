package org.onetwo;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import net.sourceforge.sizeof.SizeOf;


public class Test {

	public static void main(String[] args) throws IOException{
//		SizeOf.turnOnDebug();
		ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
		URL[] urls = ((URLClassLoader) loader).getURLs();
        for (int i=0; i<urls.length; i++) {
        	System.out.println("url: " + urls[i]);
        }
	}
	
}
