package test.entity;

import org.onetwo.common.utils.LangUtils;



public class Test {

	public static void main(String[] args){
		String s1="123,123, ,";
		String[] ss = s1.split(",");
		for (int i = 0; i < ss.length; i++) {
			System.out.println(i+":"+ss[i]);
		}
	}
}
