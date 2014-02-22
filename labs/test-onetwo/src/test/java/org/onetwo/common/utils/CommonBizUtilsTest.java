package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class CommonBizUtilsTest {
	
	@Test
	public void testGeneratedIdCardNo(){
		for (int i = 0; i < 1000; i++) {
			String cardNo = CommonBizUtils.generatedIdCardNo();
			boolean rs = CommonBizUtils.checkIdCardNo(cardNo);
			if(!rs){
				System.out.println("cardno["+cardNo+"] error");
			}
			Assert.assertTrue(rs);
		}
	}
	
	

}
