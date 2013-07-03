package org.onetwo.common.utils.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class MDFactoryTest {
	
	@Test
	public void testMD(){
		String source = "123456";
		String encrypt = MDFactory.getMDEncrypt().encryptWithSalt(source);
		LangUtils.println("after encrypt : ${0}", encrypt);
		
		boolean rs = MDFactory.checkEncrypt(source, encrypt);
		Assert.assertEquals(true, rs);
	}
	
	@Test
	public void testMDWithoutLabel(){
		String source = "123";
		String encrypt = MDFactory.createMD5(false, false).encrypt(source);
		LangUtils.println("testMDWithoutLabel after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		encrypt = MDFactory.createMD5(false, false).encryptWithSalt(source);
		LangUtils.println("testMDWithoutLabel after encryptWithSalt : ${0}, ${1}", encrypt.length(), encrypt);
		
		boolean rs = MDFactory.createMD5(false, true).checkEncrypt(source, encrypt);
		Assert.assertEquals(true, rs);
	}

}
