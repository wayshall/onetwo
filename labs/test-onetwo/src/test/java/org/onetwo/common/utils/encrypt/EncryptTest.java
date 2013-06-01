package org.onetwo.common.utils.encrypt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MD5;

public class EncryptTest {
	
	private MD5 md5;
	
	@Before
	public void setup(){
		md5 = new MD5();
	}
	
	@Test
	public void testSSha(){
		String source = "18a502bf858af364eb533e435458f8ec21122893173720111226590026804%E4%B8%9C%E9%83%A8%E5%8D%8E%E4%BE%A8%E5%9F%8E%EF%BC%88%E4%BB%85%E4%BE%9B%E6%B5%8B%E8%AF%95%E4%B9%8B%E7%94%A8%EF%BC%89%E8%87%AA%E9%A9%BE%E5%8D%A1%2B%E5%A4%A7%E5%B3%A1%E8%B0%B7%E8%B5%A0%E7%A5%A8%E9%95%BF%E9%9A%86%E6%B0%B4%E4%B8%8A%E4%B8%96%E7%95%8C%E6%B5%8B%E8%AF%95%E4%B9%8B%E7%94%A8";
		LangUtils.println("md5: " + new MDEncryptImpl("MD5", 16, false, "GBK").encrypt(source));
//		String str = MDFactory.getMDEncrypt("")
		boolean rs = MDFactory.checkEncrypt(source, "{SSHA}dRJ8DubIK32EuXymucr00X2V6BcMYqNfzCyK6g==");
		System.out.println("rs: " + rs);
	}
	
	@Test
	public void testMD5(){
		String source = "vip";
		String str = MDFactory.getMDEncrypt("md5").encrypt(source);
		LangUtils.println("md5: "+str);
		str = MDFactory.getMDEncrypt("md5").encryptWithSalt(source);
		LangUtils.println("md5 with salt: "+str);
		
		MDEncrypt md5 = new MDEncryptImpl("MD5", 16, false);
		str = md5.encrypt(source);
		LangUtils.println("md5: "+str);
		
		str = md5.encryptWithSalt(source);
		LangUtils.println("md5 with salt: "+str);
	}
	
//	@Test
	public void testMDEncrypt2(){
		String source = "123456password";
		Assert.assertTrue(MDFactory.checkEncrypt(source, "44BF025D27EEA66336E5C1133C3827F7"));
		Assert.assertTrue(MDFactory.checkEncrypt(source, "{MD5}RL8CXSfupmM25cETPDgn9w=="));
		Assert.assertTrue(MDFactory.checkEncrypt(source, "{SMD5}r9+wTtrQ1oGqE89mF7zPcfafpBe2ABM5"));
		Assert.assertTrue(MDFactory.checkEncrypt(source, "{SHA}UFLC+biTtu1vqz2SSxc5UidQ5Hs="));
		Assert.assertTrue(MDFactory.checkEncrypt(source, "{SSHA}f3KEVxmU7qX95i4tTuc+llJC00RiPPa6QkkYEQ=="));
		Assert.assertTrue(MDFactory.checkEncrypt(source, "{CLEARTEXT}123456password"));
		
		
		String str = "";
		str = md5.calcMD5(source);
		LangUtils.println("md5aa: "+str);
		Assert.assertTrue(LangUtils.isHexString(str));
		
		str = MDFactory.getMDEncrypt("md5").encrypt(source);
		LangUtils.println("md5: "+str);
		str = MDFactory.getMDEncrypt("md5").encryptWithSalt(source);
		LangUtils.println("smd5: "+str);
		str = MDFactory.getMDEncrypt("sha").encrypt(source);
		LangUtils.println("sha1: "+str);
		str = MDFactory.getMDEncrypt("sha").encryptWithSalt(source);
		LangUtils.println("ssha1: "+str);
		System.out.println("hexstring: " + LangUtils.isHexString(str));

		
		Assert.assertFalse(LangUtils.isHexString(str));
	}

}
