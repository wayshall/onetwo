package org.onetwo;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;



public class Test {
	
	public static void main(String[] args){
//		String[] strs = new String[]{"post"};
		String[] strs = new String[]{null};
//		strs = null;
		boolean rs = ObjectUtils.isArray(strs);
		System.out.println(rs);
		if(!ObjectUtils.isEmpty(strs) && StringUtils.hasLength(strs[0])){
			System.out.println("str: " + strs[0]);
		}
	}
	
}
