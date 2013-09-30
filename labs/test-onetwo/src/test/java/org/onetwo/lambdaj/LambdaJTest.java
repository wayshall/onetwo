package org.onetwo.lambdaj;

import static ch.lambdaj.Lambda.*;

import org.junit.Test;

import ch.lambdaj.function.closure.Closure;

public class LambdaJTest {

	@Test
	public void test(){
		Closure println = closure();{
			of(System.out).println(var(String.class));
		};
		println.apply("test");
		println.each("aa", "bb", "cc");
	}
}
