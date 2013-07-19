package org.onetwo.common.rest;

import java.util.Map;

import org.junit.Test;
import org.onetwo.common.rest.vo.AddMdmParams;
import org.onetwo.common.rest.vo.MdmAddResult;
import org.onetwo.common.spring.rest.JFishRestTemplate;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.ReflectUtils;

public class JFishRestTemplateTest {
	JFishRestTemplate restTemplate = new JFishRestTemplate();

	@Test
	public void testAdd(){
		AddMdmParams addParams = new AddMdmParams();
		//base
		addParams.setService("order_ticket");
		addParams.setUserkey("486888a9311a4fa4a3fbb74d28658389");
		addParams.setMembername("测试");
		
		addParams.setBukey("128f2c954bde464ebd6f3b9534d6660e");
		addParams.setAlipaykey("alipaykey");
		//自动生成
//		addParams.setItemordersno("订单编号");
		addParams.setTicketsname("测试景点");
		addParams.setOrderdate(NiceDate.New().formatAsDateTime());
		addParams.setCustomername("曾先生");
		addParams.setContactphone("13622267218");
		addParams.setQuantity("11");
		addParams.setChildqty("0");
		addParams.setCreatedate(NiceDate.New().formatAsDateTime());
		addParams.setRd(NiceDate.New().format("HH:mm"));
		addParams.setAlipaykey("alipaykey");
		addParams.setReturnurl("http://183.63.137.171:8191/emall-api/outer/mdm/add_callback.do");
		
		
//		String rs = mdmRestServiceImpl.testForAddMdm(addParams);
		MdmAddResult result = restTemplate.post("http://localhost:8080/emall-api/outer/mdm/add.json", addParams, MdmAddResult.class);
		LangUtils.println("result:${0}", result);
	}
}
