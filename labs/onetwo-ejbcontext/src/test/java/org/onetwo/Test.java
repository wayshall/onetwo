package org.onetwo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("ejb.xml");
		String[] beanNames = app.getBeanDefinitionNames();
		String result = "";
		if(beanNames!=null){
			if(beanNames!=null){
				StringBuilder sb = new StringBuilder();
				for(String bn : beanNames){
					if(!bn.endsWith("SLSB"))
						continue;
					sb.append(bn).append("\n");
				}
				result = sb.toString();
			}
		}
		System.out.println(result);
	}

}
