package org.onetwo.common.typeresolver;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.jodah.typetools.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
public class TypeResolverTest {
	
	@Test
	public void test(){
		List<Integer> list = new ArrayList<Integer>();
		Class<?> type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
		
		list = new ArrayList<Integer>(){};
		type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
	}

}
