package org.onetwo.common.spring.rest;

import java.io.File;
import java.net.URL;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.rest.TestData;
import org.onetwo.plugins.rest.TestData2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class RestTemplateTest {
	
	private JFishRestTemplate restTemplate;
	
	@Before
	public void init() {
		this.restTemplate = new JFishRestTemplate();
	}

	/*******
	 * 
	 * @RequestMapping(value="/testupload", method=RequestMethod.POST)
	public Object upload(String name, MultipartFile file){
		System.out.println("name: " + name);
		System.out.println("file: " + file.getName());
		return json(LangUtils.asMap("result", "succeed"));
	}
	 */
//	@Test
	public void testUploadFile() {
		String url = "http://localhost:8080/tel_travel/upload.do";
		final MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
		HttpHeaders headers = RestUtils.createHeader(MediaType.MULTIPART_FORM_DATA);
		
		URL fileUrl = FileUtils.getClassLoader().getResource("card.xlsx");
		File file = new File(fileUrl.getFile());
		Resource res = new FileSystemResource(file);
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("name", "excel文档");
		parts.add("file", res);
		
		HttpEntity<Map> entity = new HttpEntity<Map>(parts, headers);
		ResponseEntity<HashedMap> response = this.restTemplate.postForEntity(url, entity, HashedMap.class);
		Object result = response.getBody();
		System.out.println("result: " + result);
	}
	
//	@Test
	public void testRest(){
		String url = "http://localhost:8080/member/test.json";
		TestData data = new TestData();
		data.setName("data");
		data.setStrs(LangUtils.newArrayList("aa", "bb"));
		data.getMaps().put("map1", 1);
		data.getMaps().put("map2", LangUtils.newArrayList(1, 2, 3));

		TestData2 d2 = new TestData2();
		d2.setName("d2");
		TestData2 d3 = new TestData2();
		d2.setName("d3");
		data.setDatalist(LangUtils.newArrayList(d2, d3));
		data.getMaps().put("map3", d2);
//		this.restTemplate.post(url, data, RestResult.class);
		
		Object obj = RestUtils.toMultiValueMap(data);
		System.out.println(obj);
		Assert.assertEquals("{dataId=[0], datalist[0].dataId=[0], datalist[0].name=[d3], datalist[1].dataId=[0], maps.map3.dataId=[0], maps.map3.name=[d3], maps.map2[0]=[1], maps.map2[1]=[2], maps.map2[2]=[3], maps.map1=[1], name=[data], strs[0]=[aa], strs[1]=[bb]}", obj.toString());
		
		obj = RestUtils.toMultiValueMap(LangUtils.asMap("aa", 11, "bb", 22));
		System.out.println(obj);
		Assert.assertEquals("{aa=[11], bb=[22]}", obj.toString());
	}
	
//	@Test
	public void testLackFieldsJson(){
		RestTemplate rest = null;
		
		rest = new RestTemplate();
		String url = "http://172.20.80.31:8080/mall-member/inner/member_info_get.json";
		try {
			Result jsonmap = rest.getForObject(url, Result.class);
			System.out.println("json: " + jsonmap);
			Assert.fail("should failed");
		} catch (Exception e) {
			Assert.assertTrue(HttpMessageNotReadableException.class.isInstance(e));
		}

		rest = new FixRestTemplate();
		Result jsonmap = rest.getForObject(url, Result.class);
		System.out.println("json: " + jsonmap);
	}
	
	@Test
	public void testPlistJson(){
		MultiValueMap<String, String> urlVars = new LinkedMultiValueMap<String, String>();
		urlVars.set("page_no", "1");
		urlVars.set("page_size", "8");
		urlVars.set("system_code", "bbb");
		urlVars.set("occasion_id", "bbb");
		urlVars.set("sale_count_type", "0");
		urlVars.set("compose_mode", "1");
		System.out.println("urlVars:"+urlVars);
		RestTemplate rest = new RestTemplate();
		String url = "http://172.20.80.135:8080/emall-api/inner/product_sale_plist.json";
		Map jsonmap = rest.postForObject(url, urlVars, Map.class);
		System.out.println("json: " + jsonmap);
//		jsonmap = new JFishRestTemplate().post(url, urlVars, Map.class);
//		System.out.println("json: " + jsonmap);
	}
	
	public static class Result {
		private String ret_flag;

		public String getRet_flag() {
			return ret_flag;
		}

		public void setRet_flag(String ret_flag) {
			this.ret_flag = ret_flag;
		}
		
	}
}
