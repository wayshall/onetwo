package org.onetwo.test;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class UrlCodeTest {
	
	@Test
	public void testCode() throws IOException{
		String str = "redirect:%25{(new+java.lang.ProcessBuilder(new+java.lang.String[]{'ls'})).start";
		System.out.println(URLDecoder.decode(str));
		
		str = "redirect:%{(new+java.lang.ProcessBuilder(new+java.lang.String[]{'ls'})).start";
		System.out.println(URLEncoder.encode(str));
		System.out.println(URLDecoder.decode(URLEncoder.encode(str)));
		
		ProcessBuilder pb = new ProcessBuilder(new String[]{"calc"});
		pb.start();
	}

}
