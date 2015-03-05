package org.onetwo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class Test {

	public static void main(String[] args) throws Exception {
		Class clazz = Class.forName("org.onetwo.Py");
		Method m = clazz.getMethod("exec", Class.forName("java.util.HashMap"));
		HashMap map = new HashMap();
		map.put("1", "aaa");
		map.put("2", "bbb");
		map.put("3", "ccc");
		Py py = new Py();
		m.invoke(py, map);
		py.exec(map);
	}
	
}
class Py {

	public void exec(Map map){
		System.out.println(map);
	}

	/*public void exec(HashMap map){
		System.out.println(map);
	}*/
}