package org.onetwo.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.delegate.DelegateFactory;
import org.onetwo.common.utils.delegate.DelegateMethod;

public class Timeit {
	
	public static final Timeit timer = new Timeit();
	
	private Map<String, Long> times = new LinkedHashMap<String, Long>();

	public void ptime(String name, Block block){
		System.out.println("timeit ["+name+"] : " + timeit(name, block));
	}
	public Timeit timeit(String name, Block block){
		long start = System.currentTimeMillis();
		block.execute();
		long cost = System.currentTimeMillis() - start;
		times.put(name, cost);
		return this;
	}
	
	public void ptimeit(Object obj, String method){
		System.out.println("timeit ["+obj.getClass().getSimpleName()+"."+method+"] : " + timeit(obj, method));
	}
	
	public Timeit timeit(Object obj, String method){
		String name = obj.getClass().getSimpleName()+"."+method;
		DelegateMethod dmethod = DelegateFactory.create(obj, method);
		long start = System.currentTimeMillis();
		dmethod.invoke();
		long cost = System.currentTimeMillis() - start;
		times.put(name, cost);
		return this;
	}
	
	public void printAll(){
		for(Entry<String, Long> entry : times.entrySet()){
			System.out.println("timeit ["+entry.getKey()+" : " + entry.getValue());
		}
		this.times.clear();
	}
}
