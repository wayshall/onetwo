package org.onetwo.common.profiling;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.delegate.DelegateFactory;
import org.onetwo.common.delegate.DelegateMethod;
import org.onetwo.common.utils.func.ArgsReturnableClosure;

public class Timeit {
	
	public static final Timeit create(){
		return new Timeit();
	}
	
	public static final Timeit create(TimeLogger ouputer){
		return new Timeit(ouputer);
	}
	
	private Map<String, Long> times = new LinkedHashMap<String, Long>();

    private TimeLogger out;
    
	private Timeit() {
		this.out = new TimerOutputer();
	}
	private Timeit(TimeLogger ouputer) {
		super();
		this.out = ouputer;
	}
	public void ptime(String name, ArgsReturnableClosure block){
		out.log(Timeit.class, "timeit ["+name+"] : " + timeit(name, block));
	}
	public Timeit timeit(String name, ArgsReturnableClosure block){
		long start = System.currentTimeMillis();
		block.execute();
		long cost = System.currentTimeMillis() - start;
		times.put(name, cost);
		return this;
	}
	
	public void ptimeit(Object obj, String method){
		out.log(Timeit.class, "timeit ["+obj.getClass().getSimpleName()+"."+method+"] : " + timeit(obj, method));
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
			out.log(Timeit.class, "timeit ["+entry.getKey()+" : " + entry.getValue());
		}
		this.times.clear();
	}
}
