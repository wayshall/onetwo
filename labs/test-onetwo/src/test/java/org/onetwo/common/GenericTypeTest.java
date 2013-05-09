package org.onetwo.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericTypeTest {

	public static void main(String[] args){
		List<Timestamp> list = new ArrayList<Timestamp>();  
	    Date date = new Date();  
	    Timestamp time = new Timestamp(date.getTime());  
	    upperBound2(list,time);  
	}
	

	public static <T extends Date> void upperBound2(List<T> list, T date)  
	{  
	    list.add(date);  
	} 
}
