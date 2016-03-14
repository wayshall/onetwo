package org.onetwo.common.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.Test;

public class SimpleTest {
	
	@Test
	public void test(){
		Method[] methods = Serializable.class.getDeclaredMethods();
		Stream.of(methods).map(m->m.getName()).forEach(System.out::println);
		
		methods = Iterable.class.getDeclaredMethods();
		Stream.of(methods).map(m->m.getName()).forEach(System.out::println);
	}

}
