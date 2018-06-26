package org.onetwo.common.spring;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.core.ResolvableType;

/**
 * @author wayshall
 * <br/>
 */
public class SpringResolvableTypeTest {
	public static class SubList extends ArrayList<Integer> {
	}
	@Test
	public void test(){
		ResolvableType rt = ResolvableType.forClass(SubList.class);
		System.out.println("rt:"+rt.as(List.class).getGeneric(0).resolve());
		
		List<Integer> list2 = new ArrayList<Integer>(){};
		rt = ResolvableType.forClass(list2.getClass());
		System.out.println("rt:"+rt.as(List.class).getGeneric(0).resolve());
		

		list2 = new ArrayList<Integer>();
		rt = ResolvableType.forClass(list2.getClass());
		System.out.println("rt:"+rt.as(List.class).getGeneric(0).resolve());
	}
}
