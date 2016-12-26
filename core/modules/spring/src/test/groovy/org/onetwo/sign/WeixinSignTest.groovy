package org.onetwo.sign

import org.junit.Test;
import org.onetwo.common.encrypt.MDFactory;
import org.onetwo.common.utils.ParamUtils;
import org.onetwo.common.xml.XmlUtils;

class WeixinSignTest {
	
	@Test
	def void testSign(){
		def xml = """
			<xml>
				<appid><![CDATA[wx2484d42787997afc]]></appid>
				<attach><![CDATA[1148980]]></attach>
				<bank_type><![CDATA[CMB_DEBIT]]></bank_type>
				<cash_fee><![CDATA[18900]]></cash_fee>
				<fee_type><![CDATA[CNY]]></fee_type>
				<is_subscribe><![CDATA[N]]></is_subscribe>
				<mch_id><![CDATA[10029967]]></mch_id>
				<nonce_str><![CDATA[r]]></nonce_str>
				<openid><![CDATA[oMLl-jicxim7EC5A-Np2q_uu05e8]]></openid>
				<out_trade_no><![CDATA[1479277576032983]]></out_trade_no>
				<result_code><![CDATA[SUCCESS]]></result_code>
				<return_code><![CDATA[SUCCESS]]></return_code>
				<sign><![CDATA[398DFBD65C8550ABACD7583B50A04F40]]></sign>
				<time_end><![CDATA[20161116160807]]></time_end>
				<total_fee>18900</total_fee>
				<trade_type><![CDATA[JSAPI]]></trade_type>
				<transaction_id><![CDATA[4006192001201611169912675325]]></transaction_id>
			</xml>
		"""
		XmlObj obj = XmlUtils.toBean(xml, "xml", XmlObj.class)
		println("obj: ${obj}")
		String toParamString = ParamUtils.comparableKeyMapToParamString(obj)
		println("toParamString: ${toParamString}")
		boolean res = MDFactory.createMD5().checkEncrypt(toParamString, "398DFBD65C8550ABACD7583B50A04F40")
		println("res: ${res}")
	}
	
	class XmlObj {
		String appid;
		String attach;
		String bank_type;
		String cash_fee;
		String fee_type;
		String is_subscribe;
		String mch_id;
		String nonce_str;
		String openid;
		String out_trade_no;
		String result_code;
		String return_code;
		String time_end;
		String total_fee;
		String trade_type;
		String transaction_id;
	}

}
