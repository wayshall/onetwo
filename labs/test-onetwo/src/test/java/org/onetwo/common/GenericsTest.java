package org.onetwo.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.entity.UserEntity;

public class GenericsTest {

	public static void main(String[] args){
		List<?> list = new ArrayList();
		list.add(new UserEntity());
		list.add(null);
		
		List<Number> numberlist = new ArrayList<Number>();
		numberlist.add(Integer.valueOf(1));
		
		List<? extends Number> extnumberlist = new ArrayList<Number>();
		extnumberlist.add(new Integer(1));

		testMehtod(new Integer[]{});
		testMehtod(new ArrayList<Integer>());
		
		List<String>[] lsa = new List<String>[10]; // illegal 
		 Object[] oa = lsa;  // OK because List<String> is a subtype of Object 
		 List<Integer> li = new ArrayList<Integer>(); 
		 li.add(new Integer(3)); 
		 oa[0] = li; 
		 String s = lsa[0].get(0); 
	}
	
	public static void testMehtod(Number[] numbers){
		
	}
	
	public static void testMehtod(List<Number> numbers){
		
	}
	public void upperBound(List<? extends Date> list, Date date)  
	{  
	    Date now = list.get(0);  
	    System.out.println("now==>" + now);  
	    list.add(date); //这句话无法编译  
	    list.add(null);//这句可以编译，因为null没有类型信息  
	}
	
	public void lowerBound(List<? super Timestamp> list)  
	{  
	    Timestamp now = new Timestamp(System.currentTimeMillis());  
	    list.add(now);  
	    Timestamp time = list.get(0); //不能编译  
	} 
	
}
