package org.onetwo.common.encrypt;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.md.CodeType;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.md.Hashs.MessageDigestHasherBuilder;
import org.onetwo.common.md.MessageDigestHasher;
import org.onetwo.common.utils.LangUtils;

public class HashsTest {
	
	/****
	 * 把原始的密码字符串用utf8编码成byte数组，加8位随机字节，然后用sha算法散列。
把散列结果+加上之前的8位随机字节后，在用base64编码。即：
base64( sha(to_byte_array(source)+salt) + salt )
salt为8个随机字节。
	 */
	@Test
	public void testMD(){
		String source = "123456";
		String encrypt = Hashs.getMDEncrypt().hashWithRandomSalt(source, -1);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		boolean rs = Hashs.checkEncrypt(source, encrypt);
		Assert.assertEquals(true, rs);
		
		encrypt = Hashs.getMDEncrypt().hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		rs = Hashs.checkEncrypt(source, encrypt);
		Assert.assertEquals(true, rs);

		MessageDigestHasher sha = Hashs.SHA;
		encrypt = sha.hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		
		rs = sha.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
		


		MessageDigestHasher sha1 = MessageDigestHasherBuilder.newBuilder("SHA")
																.size(20)
																.withLabel(true)
																.build();
		encrypt = sha1.hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		
		rs = sha1.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
	}
	
	@Test
	public void testCsrf(){
		String source = "77352BFB8E1D171486DE6E4AC7CBE53D";
		MessageDigestHasher md5 = Hashs.MD5;
		
		String encrypt = md5.hashWithRandomSalt(source, -1);
		LangUtils.println("testMDWithoutLabel after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		boolean rs = md5.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
		
		
		encrypt = "36D69B4433C402F087A666A004B6A8ECD9BCD663C68F3700";
		LangUtils.println("testMDWithoutLabel after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		rs = md5.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
	}
	@Test
	public void testMDWithoutLabel(){
		String source = "12356";
		String encrypt = MessageDigestHasherBuilder.newBuilder("MD5")
													.size(16)
													.withLabel(false)
													.codeType(CodeType.HEX)
													.build()
													.hash(source);
		LangUtils.println("testMDWithoutLabel after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		encrypt = MessageDigestHasherBuilder.newBuilder("MD5")
											.size(16)
											.withLabel(false)
											.codeType(CodeType.HEX)
											.build()
											.hashWithRandomSalt(source, -1);
		LangUtils.println("testMDWithoutLabel after encryptWithSalt : ${0}, ${1}", encrypt.length(), encrypt);
		
		boolean rs = MessageDigestHasherBuilder.newBuilder("MD5")
				.size(16)
				.withLabel(true)
				.codeType(CodeType.HEX)
				.build()
				.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
		
		encrypt = Hashs.getMDEncrypt("SMD5").hashWithRandomSalt(source, -1);
		LangUtils.println("smd5 after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
	}
	

	@Test
	public void testSha(){
		String source = "123456";
		String encrypt = Hashs.getMDEncrypt("SHA").hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		boolean rs = Hashs.getMDEncrypt("SHA").checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
		
		encrypt = Hashs.getMDEncrypt("SHA").hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);

		MessageDigestHasher sha = MessageDigestHasherBuilder.newBuilder("SHA")
															.size(20)
															.withLabel(false)
															.codeType(CodeType.HEX)
															.build();
		encrypt = sha.hash(source);
		LangUtils.println("after encrypt : ${0}, ${1}", encrypt.length(), encrypt);
		
		rs = sha.checkHash(source, encrypt);
		Assert.assertEquals(true, rs);
	}


	@Test
	public void testSign(){
		String DEFAULT_SSO_SIGN_KEY = "asdfa7sd9fa[ko@#$s0df]pips9";
		String source = LangUtils.appendNotBlank("6RmyQ2 t1KFIIvAijFE9wqPFMXNwfSXMiKDvEg==", DEFAULT_SSO_SIGN_KEY);
		String entrystr = Hashs.MD5.hashWithRandomSalt(source, -1);
//		boolean rs = MDFactory.MD5.checkEncrypt(source, "4B4AB0CF1EB5DD19D8B7C24F0D0626F20085AD9EA9D03D02");
		boolean rs = Hashs.MD5.checkHash(source, entrystr);
		Assert.assertEquals(true, rs);
	}

	@Test
	public void generate(){
		String source = "test";
		String salt = "test";
		String entrystr = Hashs.MD5.hashWithSalt(source, salt);
		System.out.println("generate: " + entrystr);
//		boolean rs = Hashs.MD5.checkHash(source, "4B4AB0CF1EB5DD19D8B7C24F0D0626F20085AD9EA9D03D02");
		boolean rs = Hashs.MD5.checkHash(source, entrystr);
		Assert.assertEquals(true, rs);
		entrystr = Hashs.MD5.hash(source+salt);
		rs = Hashs.MD5.checkHash(source, salt, entrystr);
		Assert.assertEquals(true, rs);
	}

}
