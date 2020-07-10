package org.onetwo.common.typeresolver;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;

import net.jodah.typetools.TypeResolver;

/**
 * @author wayshall
 * <br/>
 */
public class TypeResolverTest {
	
	private List<Integer> intList;
	
	@Test
	public void test(){
		List<Integer> list = new ArrayList<Integer>();
		Class<?> type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
		
		list = new ArrayList<Integer>(){};
		type = TypeResolver.resolveRawArgument(List.class, list.getClass());
		System.out.println("type:"+type);
		
		Class<?> propertyType = Intro.wrap(this.getClass()).getProperty("intList").getPropertyType();
		System.out.println("propertyType: " + propertyType);
		
		Class<?> listItemType = ReflectUtils.getListGenricType(propertyType);
		System.out.println("listItemType: " + listItemType);
		
	}

	public List<Integer> getIntList() {
		return intList;
	}

	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}
	
	

}
