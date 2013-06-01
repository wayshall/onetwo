package org.onetwo.common.utils.map;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;


public class MapPerformanceTest {

	private Map<String, String> maps = LangUtils.newMap();
	private List<String> datalist = LangUtils.newArrayList();
	private Set<String> sets = new HashSet<String>();

	String str = "teststring";
	int dataCount = 100000;
	int startIndex = dataCount/2;
	
	@Before
	public void setup(){
		String val = null;
		for (int i = 0; i < dataCount; i++) {
			val = str+i;
			maps.put(val, null);
			datalist.add(val);
			sets.add(val);
		}
	}
	
	@Test
	public void testContains(){
		UtilTimerStack.setActive(true);
		
		this.testMapContains(1000);
		this.testListContains(1000);
		this.testSetContains(1000);
	}
	
	public void testMapContains(int count){
		String name = "mapcontains";
		UtilTimerStack.push(name);
		for (int i = 0; i < count; i++) {
			this.maps.containsKey(str+(startIndex+i));
		}
		UtilTimerStack.pop(name);
	}
	
	public void testSetContains(int count){
		String name = "setcontains";
		UtilTimerStack.push(name);
		for (int i = 0; i < count; i++) {
			this.sets.contains(str+(startIndex+i));
		}
		UtilTimerStack.pop(name);
	}
	
	public void testListContains(int count){
		String name = "listcontains";
		UtilTimerStack.push(name);
		for (int i = 0; i < count; i++) {
			this.datalist.contains(str+(startIndex+i));
		}
		UtilTimerStack.pop(name);
	}

}
