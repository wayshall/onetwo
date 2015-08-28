package org.onetwo.common.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class MDEncryptImpl implements MDEncrypt {
//	public static final String BASE64_POSTFIX = "-B64";
	
	private String algorithm;
	private MessageDigest md;
	
	private String charset;
	private boolean base64;
	private int size;
	
//	private boolean isDev = false;

	MDEncryptImpl(String algorithm, int size){
		this(algorithm, size, false);
	}

	MDEncryptImpl(String algorithm, int size, boolean base64){
		this(algorithm, size, base64, "UTF-8");
	}
	MDEncryptImpl(String algorithm, int size, boolean base64, String charset){
		this.algorithm = algorithm.trim();
		try {
			this.md = MessageDigest.getInstance(this.algorithm);
			this.size = size;
			this.base64 = base64;
			this.charset = charset;
		} catch (NoSuchAlgorithmException e) {
			LangUtils.throwServiceException(e);
		}
	}
	
	public int getSize() {
		return size;
	}

	public String getLabel(boolean withSalt){
		return "{"+(withSalt?"S":"")+algorithm+"}";
	}
	
	protected byte[] getBytes(final String str){
		return LangUtils.getBytes(str, charset);
	}
	
	protected String encode(byte[] bytes){
		String result = "";
		if(base64){
			result = LangUtils.newString(Base64.encodeBase64(bytes), charset);
		}
		else{
			result = LangUtils.toHex(bytes);
		}
		return result;
	}
	
	protected byte[] decode(String str){
		byte[] result = null;
		if(base64){
			result = Base64.decodeBase64(getBytes(str));
		}else{
			result = LangUtils.hex2Bytes(str);
		}
		return result;
	}
	
	protected String appendLabel(String encryptStr, boolean withSalt){
		if(!isWithLabel())
			return encryptStr;
		String lable = getLabel(withSalt);
		if(encryptStr.startsWith(lable))
			return encryptStr;
		return getLabel(withSalt)+encryptStr;
	}
	
	protected byte[] randomSalt(){
		return randomSalt(8);
	}
	
	protected byte[] randomSalt(int saltLength){
		int len = saltLength;
		if(saltLength<1)
			len = 8;
		byte[] bytes = new byte[len];
		for(int i=0; i<len; i++){
			bytes[i] = (byte)((Math.random()*256)-128);
		}
		return bytes;
	}
	
	public String encrypt(String source){
		String encryptStr = encrypt(getBytes(source), null);
//		return encrypt(getBytes(source), randomSalt());
		return appendLabel(encryptStr, false);
	}

	/*****
	 * byte[] = digest(source+salt)+salt;
	 * String = hex(byte[]);
	 */
	public String encryptWithSalt(String source, int saltLength){
		String encryptStr = encrypt(getBytes(source), randomSalt(saltLength));
		return appendLabel(encryptStr, true);
	}
	public String encryptWithSalt(String source, byte[] salt){
		byte[] digest = _encryptBytesOnly(getBytes(source), salt);
		String encryptString = encode(digest);
		return appendLabel(encryptString, true);
	}
	public String encryptWithSalt(String source, String salt){
		return encryptWithSalt(source, getBytes(salt));
	}
	
	public String encryptWithSalt(String source){
		String encryptStr = encrypt(getBytes(source), randomSalt());
		return appendLabel(encryptStr, true);
	}
	
	public String encrypt(byte[] source, byte[] salt){
		byte[] digest = encryptBytes(source, salt);
//		byte[] digest = _encryptBytesOnly(source, salt);
//		System.out.println("encrypt:" + encode(digest));
//		digest = mergeSalt(digest, salt);
		String encryptString = encode(digest);
		return encryptString;
	}
	
	/****
	 * byte[] = digest(source+salt)+salt
	 */
	public byte[] encryptBytes(byte[] source, byte[] salt){
		byte[] dg = _encryptBytesOnly(source, salt);
		boolean hasSalt = (salt!=null && salt.length>0);
		if(hasSalt){
			dg = mergeSalt(dg, salt);
		}
		return dg;
	}
	
	/*****
	 * 先源字符串，后随机盐
	 * @param source
	 * @param salt
	 * @return
	 */
	synchronized public byte[] _encryptBytesOnly(byte[] source, byte[] salt){
		boolean hasSalt = (salt!=null && salt.length>0);
		byte[] dg = null;
		md.reset();
		md.update(source);
		
		if(hasSalt){
			md.update(salt);
		}
		
		dg = md.digest();
		
//		System.out.println("hex: " + toHex(dg));
		return dg;
	}
	
	/*****
	 * byte(entryHexString)=> hash = entry + salt, 得到salt 
	 * boolean: digest(source+salt) == hash
	 */
	public boolean checkEncrypt(String source, String encrypt){
//		LangUtils.println(isDev, "checkEncrypt start=============================>>>");
		Assert.hasText(source);
		if(StringUtils.isBlank(encrypt))
			return false;
		boolean valid = false;
		byte[] hash;
		
		String trimLable = encrypt;
		String label = MDEncryptUtils.getLabel(encrypt);
		if(StringUtils.isBlank(label) && LangUtils.isHexString(encrypt)){
			hash = LangUtils.hex2Bytes(trimLable);
		}else{
			trimLable = MDEncryptUtils.trimLabel(encrypt);
			hash = decode(trimLable);
		}
		byte[][] decodeBytes = splitSalt(hash, size);
		hash = decodeBytes[0];
		byte[] salt = decodeBytes[1];
		
		if(hash==null)
			return false;
		
		byte[] sourceEncoded = _encryptBytesOnly(getBytes(source), salt);
		valid = MessageDigest.isEqual(hash, sourceEncoded);
//		LangUtils.println(isDev, "checkEncrypt end=============================>>>");
		
		return valid;
	}
	

	public boolean checkEncrypt(String source, String saltStr, String encrypt){
		Assert.hasText(source);
		if(StringUtils.isBlank(encrypt))
			return false;
		byte[] hash;
		
		String trimLable = encrypt;
		String label = MDEncryptUtils.getLabel(encrypt);
		if(StringUtils.isBlank(label) && LangUtils.isHexString(encrypt)){
			hash = LangUtils.hex2Bytes(trimLable);
		}else{
			trimLable = MDEncryptUtils.trimLabel(encrypt);
			hash = decode(trimLable);
		}
		if(hash==null)
			return false;
		
		byte[] sourceEncoded = _encryptBytesOnly(getBytes(source), getBytes(saltStr));
		return MessageDigest.isEqual(hash, sourceEncoded);
	}
	
	protected byte[] mergeSalt(byte[] source, byte[] salt){
		return ArrayUtils.addAll(source, salt);
//		return source;
	}
	
	protected byte[][] splitSalt(byte[] source, int size){
		byte[] encrypt, salt;
		if(source==null || source.length<=size){
			encrypt = source;
			salt = new byte[0];
		}else{
			encrypt = new byte[size];
			salt = new byte[source.length - size];
			System.arraycopy(source, 0, encrypt, 0, size);
			System.arraycopy(source, size, salt, 0, salt.length);
		}
		byte[][] encryptSalt = {encrypt, salt};
		return encryptSalt;
	}
	
	public boolean isWithLabel(){
		return true;
	}
	
}
