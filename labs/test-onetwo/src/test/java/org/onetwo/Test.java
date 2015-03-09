package org.onetwo;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Test {

	public static void main(String[] args) throws Exception {
		HashMap map = new HashMap();
		map.put("1", "aaa");
		map.put("2", "bbb");
		map.put("3", "ccc");
		Py py = new Py();
		py.exec(map);
		System.out.println("map: " + map);
	}
	
}
class Py {

	public void exec(Map map){
		System.out.println("exec: "+map);
		map = Collections.EMPTY_MAP;
	}

	/*public void exec(HashMap map){
		System.out.println(map);
	}*/
}