package org.onetwo.plugins.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.rest.ErrorCode;
import org.onetwo.plugins.rest.RestResult;
import org.onetwo.plugins.rest.TestData;
import org.onetwo.plugins.rest.TestData2;
import org.onetwo.plugins.rest.utils.PageData;
import org.onetwo.plugins.rest.utils.RestPluginUtils;

public class RestResultTest {
	

	@Test
	public void testRestResultFailed(){
		RestResult rs = new RestResult();
		rs.markSucceed();
		rs.setError_code(ErrorCode.COM_ER_VALIDATION);
		String json = JsonMapper.IGNORE_NULL.toJson(rs);
		System.out.println("json: " + json);
		Assert.assertEquals("{\"ret_flag\":0,\"error_code\":\"com_er_validation\"}", json);
	}
	
	@Test
	public void testRestResultSucceed(){
		RestResult rs = new RestResult();
		rs.markSucceed();
		TestData data = new TestData();
		data.setDataId(2);
		data.setName("testName");
		
		TestData2 d2 = new TestData2();
		d2.setDataId(22);
		d2.setName("testData2");
		data.setData2(d2);

		List<TestData2> dlist = new ArrayList<TestData2>();
		d2 = new TestData2();
		d2.setDataId(1);
		d2.setName("list1");
		dlist.add(d2);
		
		d2 = new TestData2();
		d2.setDataId(1);
		d2.setName("list2");
		dlist.add(d2);
		data.setDatalist(dlist);
		
		rs.setData(data);
		String json = JsonMapper.IGNORE_NULL.toJson(rs);
		System.out.println("data json: " + json);
		Assert.assertEquals("{\"ret_flag\":1,\"data\":{\"name\":\"testName\",\"dataId\":2,\"data2\":{\"name\":\"testData2\",\"dataId\":22},\"datalist\":[{\"name\":\"list1\",\"dataId\":1},{\"name\":\"list2\",\"dataId\":1}]}}", json);
	}
	
	@Test
	public void testDataField(){
		String json = "{\"ret_flag\":1,\"data\":{\"name\":\"testName\",\"dataId\":2,\"data2\":{\"name\":\"testData2\",\"dataId\":22},\"datalist\":[{\"name\":\"list1\",\"dataId\":1},{\"name\":\"list2\",\"dataId\":1}]}}";
		
		TestResult rest = JsonMapper.IGNORE_NULL.fromJson(json, TestResult.class);
		Assert.assertNotNull(rest.getData());
		Assert.assertEquals(rest.getData().getDataId(), 2);
		Assert.assertEquals("testName", rest.getData().getName());
		Assert.assertEquals(2, rest.getData().getDatalist().size());
		Assert.assertEquals("list1", rest.getData().getDatalist().get(0).getName());
	}
	
	@Test
	public void testDataFieldWithGeneric(){
		String json = "{\"ret_flag\":1,\"data\":{\"name\":\"testName\",\"dataId\":2,\"data2\":{\"name\":\"testData2\",\"dataId\":22},\"datalist\":[{\"name\":\"list1\",\"dataId\":1},{\"name\":\"list2\",\"dataId\":1}]}}";
		
		TestGenericResult rest = JsonMapper.IGNORE_NULL.fromJson(json, TestGenericResult.class);
		Assert.assertNotNull(rest.getData());
		Assert.assertEquals(rest.getData().getDataId(), 2);
		Assert.assertEquals("testName", rest.getData().getName());
		Assert.assertEquals(2, rest.getData().getDatalist().size());
		Assert.assertEquals("list1", rest.getData().getDatalist().get(0).getName());
	}

	@Test
	public void testPageData(){
		String expected = "{\"ret_flag\":1,\"data\":{\"total_count\":0,\"total_pages\":0,\"page_no\":1,\"page_size\":20,\"list\":[{\"name\":\"name0\",\"dataId\":0},{\"name\":\"name1\",\"dataId\":0},{\"name\":\"name2\",\"dataId\":0}]}}";
		Page<TestData> page = new Page<TestData>();
		for(int i=0; i<3; i++){
			TestData d = new TestData();
			d.setName("name"+i);
			page.getResult().add(d);
		}
		
		RestResult<PageData<TestData>> rest = RestPluginUtils.newPageRestResult(page);
		
		String json = JsonMapper.IGNORE_NULL.toJson(rest);
		Assert.assertEquals(expected, json);
	}
	
	@Test
	public void testPageData2(){
		String expected = "{\"ret_flag\":1,\"data\":{\"total_count\":0,\"total_pages\":0,\"page_no\":1,\"page_size\":20,\"list\":[{\"name\":\"name0\",\"dataId\":0},{\"name\":\"name1\",\"dataId\":0},{\"name\":\"name2\",\"dataId\":0}]}}";
		Page<TestData> page = new Page<TestData>();
		for(int i=0; i<3; i++){
			TestData d = new TestData();
			d.setName("name"+i);
			page.getResult().add(d);
		}
		
		RestResult<PageData<TestData>> rest = new RestResult<PageData<TestData>>();
		PageData<TestData> pageData = RestPluginUtils.newPageData(page);
		rest.setData(pageData);
		
		String json = JsonMapper.IGNORE_NULL.toJson(rest);
		Assert.assertEquals(expected, json);
	}
	

}
