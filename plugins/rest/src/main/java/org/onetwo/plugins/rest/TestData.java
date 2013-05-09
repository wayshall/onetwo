package org.onetwo.plugins.rest;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;


public class TestData {

	private String name;
	private int dataId;
	
	private TestData2 data2;
	private List<TestData2> datalist;
	private List<String> strs;
	private Map<String, Object> maps = LangUtils.newHashMap();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public TestData2 getData2() {
		return data2;
	}
	public void setData2(TestData2 data2) {
		this.data2 = data2;
	}
	public List<TestData2> getDatalist() {
		return datalist;
	}
	public void setDatalist(List<TestData2> datalist) {
		this.datalist = datalist;
	}
	public List<String> getStrs() {
		return strs;
	}
	public void setStrs(List<String> strs) {
		this.strs = strs;
	}
	public Map<String, Object> getMaps() {
		return maps;
	}
	public void setMaps(Map<String, Object> maps) {
		this.maps = maps;
	}
	
	
}
