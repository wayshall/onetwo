package org.onetwo.common.typeresolver;

import java.util.ArrayList;
import java.util.List;

import net.jodah.typetools.TypeResolver;

import org.assertj.core.util.Lists;
import org.junit.Test;

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
