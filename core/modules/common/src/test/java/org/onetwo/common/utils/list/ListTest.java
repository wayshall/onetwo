package org.onetwo.common.utils.list;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;

public class ListTest {
	
	@Test	
	public void test(){
		int count = 100000;
		List<String> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			list.add("element"+i);
		}
		
		this.add(list);
		this.addAll(list);
		this.add(list);
		this.addAll(list);
	}
	
	protected void addAll(List<String> list){
		List<String> dest = new ArrayList<String>();
		TimeCounter t = new TimeCounter("addAll");
		t.start();
		dest.addAll(list);
		t.stop();
	}
	
	protected void add(List<String> list){
		List<String> dest = new ArrayList<String>(list.size());
		TimeCounter t = new TimeCounter("add");
		t.start();
		for(String e : list){
			dest.add(e);
		}
		t.stop();
	}

}
