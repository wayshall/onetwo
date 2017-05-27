package org.onetwo.common.md;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

import com.google.common.base.Charsets;

public class MessageDigestHasherImpl implements MessageDigestHasher {
//	public static final String BASE64_POSTFIX = "-B64";
	
	private String algorithm;
	private MessageDigest md;
	
	private String charset;
//	private boolean base64;
	private int size;
	private CodeType codeType = CodeType.HEX;
//	private boolean isDev = false;
	
	private boolean withLabel;

	MessageDigestHasherImpl(String algorithm, int size){
		this(algorithm, size, Charsets.UTF_8.toString());
	}

	MessageDigestHasherImpl(String algorithm, int size, String charset){
		this.algorithm = algorithm.trim();
		try {
			this.md = MessageDigest.getInstance(this.algorithm);
			this.size = size;
//			this.base64 = base64;
			this.charset = charset;
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException("no suche algorithm found: " + this.algorithm, e);
		}
	}
	
	public int getSize() {
		return size;
	}

	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setCodeType(CodeType codeType) {
		this.codeType = codeType;
	}

	protected byte[] getBytes(final String str){
		return LangUtils.getBytes(str, charset);
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
	
	@Override
	public String hash(String source){
		return hash(getBytes(source), null);
	}

	/*****
	 * byte[] = digest(source+salt)+salt;
	 * String = hex(byte[]);
	 */
	@Override
	public String hashWithRandomSalt(String source, int saltLength){
		return hash(getBytes(source), randomSalt(saltLength));
	}
	@Override
	public String hashWithSalt(String source, byte[] salt){
		return hash(getBytes(source), salt);
	}
	@Override
	public String hashWithSalt(String source, String salt){
		return hashWithSalt(source, getBytes(salt));
	}

	/*public String encrypt(byte[] source, byte[] salt){
		byte[] digest = hash(source, salt);
//		byte[] digest = _encryptBytesOnly(source, salt);
//		System.out.println("encrypt:" + encode(digest));
//		digest = mergeSalt(digest, salt);
		String encryptString = encode(digest);
		return encryptString;
	}*/
	
	/****
	 * byte[] = digest(source+salt)+salt
	 */
	public String hash(byte[] source, byte[] salt){
		byte[] dg = hashAsBytesOnly(source, salt);
		boolean hasSalt = (salt!=null && salt.length>0);
		if(hasSalt){
			dg = mergeSalt(dg, salt);
		}
		String encoded = codeType.encode(dg, charset);
		return appendLabel(encoded, hasSalt);
	}
	
	/*****
	 * 先源字符串，后随机盐
	 * @param source
	 * @param salt
	 * @return
	 */
	synchronized private byte[] hashAsBytesOnly(byte[] source, byte[] salt){
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
	public boolean checkHash(String source, String encrypt){
//		LangUtils.println(isDev, "checkEncrypt start=============================>>>");
		Assert.hasText(source);
		if(StringUtils.isBlank(encrypt))
			return false;
		boolean valid = false;
		byte[] hash;
		
		String trimLable = encrypt;
		String label = MessageDigestHashUtils.getLabel(encrypt);
		if(StringUtils.isBlank(label)){
			hash = codeType.decode(trimLable, charset);
		}else{
			trimLable = MessageDigestHashUtils.trimLabel(encrypt);
//			hash = decode(trimLable);
			hash = codeType.decode(trimLable, charset);
		}
		byte[][] decodeBytes = splitSalt(hash, size);
		hash = decodeBytes[0];
		byte[] salt = decodeBytes[1];
		
		if(hash==null)
			return false;
		
		byte[] sourceEncoded = hashAsBytesOnly(getBytes(source), salt);
		valid = MessageDigest.isEqual(hash, sourceEncoded);
//		LangUtils.println(isDev, "checkEncrypt end=============================>>>");
		
		return valid;
	}
	
	/****
	 * 
	 */
	public boolean checkHash(String source, String saltStr, String encrypt){
		Assert.hasText(source);
		if(StringUtils.isBlank(encrypt))
			return false;
		
		byte[] hash;
		String trimLable = encrypt;
		String label = MessageDigestHashUtils.getLabel(encrypt);
		if(StringUtils.isBlank(label)){
			hash = codeType.decode(trimLable, charset);
		}else{
			trimLable = MessageDigestHashUtils.trimLabel(encrypt);
//			hash = decode(trimLable);
			hash = codeType.decode(trimLable, charset);
		}
		if(hash==null)
			return false;
		
		byte[] sourceEncoded = hashAsBytesOnly(getBytes(source), getBytes(saltStr));
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

	private String getLabel(boolean withSalt){
		return "{"+(withSalt?"S":"")+algorithm+"}";
	}
	private String appendLabel(String encryptStr, boolean withSalt){
		if(!isWithLabel())
			return encryptStr;
		String lable = getLabel(withSalt);
		if(encryptStr.startsWith(lable))
			return encryptStr;
		return getLabel(withSalt)+encryptStr;
	}
	
	public boolean isWithLabel(){
		return withLabel;
	}

	void setWithLabel(boolean withLabel) {
		this.withLabel = withLabel;
	}
	
}
