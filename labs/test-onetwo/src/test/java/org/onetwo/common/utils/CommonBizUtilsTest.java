package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class CommonBizUtilsTest {
	
	@Test
	public void testGeneratedIdCardNo(){
		for (int i = 0; i < 100; i++) {
			String cardNo = CommonBizUtils.generatedIdCardNo();
			boolean rs = CommonBizUtils.checkIdCardNo(cardNo);
			if(!rs){
				System.out.println("cardno["+cardNo+"] error");
			}
			Assert.assertTrue(rs);
		}
		for (int i = 0; i < 100; i++) {
			String cardNo = CommonBizUtils.generatedIdCardNo("广东", "1984");
			System.out.println("cardno["+cardNo+"]");
			boolean rs = CommonBizUtils.checkIdCardNo(cardNo);
			if(!rs){
				System.out.println("cardno["+cardNo+"] error");
			}
			Assert.assertTrue(rs);
		}
	}
	
	

}
