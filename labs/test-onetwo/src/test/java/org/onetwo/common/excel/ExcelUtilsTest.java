package org.onetwo.common.excel;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class ExcelUtilsTest {

	@Test
	public void testGetOgnlValue(){
		String exp = "'电子钱包'+#byMonthString+'对账单'";
		Object val = ExcelUtils.getValue(exp, LangUtils.asMap("byMonthString", "test"), null);
		System.out.println("val:" + val);
		
	}
}
