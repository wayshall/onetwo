package org.onetwo.common.spring;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

public class SpringOrderTest {
	
	@Test
	public void testOrder(){
		List<?> list = Arrays.asList(new Bean1(), new Bean2(), new BeanNullOrder(), new BeanLow(), new BeanHigh());
		AnnotationAwareOrderComparator.sort(list);
		list.forEach(System.out::println);
	}
	
	@Order(1)
	public static class Bean1 {
	}
	
	@Order(2)
	public static class Bean2 {
	}
	
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class BeanHigh {
	}
	
	@Order(Ordered.LOWEST_PRECEDENCE)
	public static class BeanLow {
	}
	
	public static class BeanNullOrder {
	}

}
