package org.onetwo.common.utils.encrypt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

abstract public class MDFactory {
	private static final Map<String, MDEncrypt> encrypts;
//	public static final String BASE64_POSTFIX = MDEncryptImpl.BASE64_POSTFIX;
	
	public static final MDEncrypt MD5 = createMD5(false, false);
	
	static {
		Map<String, MDEncrypt> map = new HashMap<String, MDEncrypt>(4);
		MDEncrypt md5 = new MDEncryptImpl("MD5", 16, true);//16bytes, 128bits
		MDEncrypt sha = new MDEncryptImpl("SHA", 20, true);//sha-1,  20bytes, 160bits
		map.put("MD5", md5);
		map.put("SHA", sha);
		map.put("SMD5", md5);
		map.put("SSHA", sha);
		map.put("CLEARTEXT", new ClearTextMDEncryptImpl());
		map.put("PLAIN", new ClearTextMDEncryptImpl("PLAIN"));
		
		/*MDEncrypt md5Base64 = new MDEncryptImpl("MD5", 16, true);
		MDEncrypt shaBase64 = new MDEncryptImpl("SHA", 20, true);
		map.put("MD5"+BASE64_POSTFIX, md5Base64);
		map.put("SHA"+BASE64_POSTFIX, shaBase64);*/
		
		encrypts = Collections.unmodifiableMap(map);
	}
	
	public static MDEncrypt getMDEncrypt(){
		return getMDEncrypt(null);
	}

	/****
	 * md5的长度为16个字节，128位
	 * @param base64
	 * @param withLabel
	 * @return
	 */
	public static MDEncrypt createMD5(boolean base64, final boolean withLabel){
		MDEncrypt encryptor = create("MD5", 16, base64, withLabel);
		return encryptor;
	}

	public static MDEncrypt create(String algorithm, int size, boolean base64, final boolean withLabel){
		MDEncryptImpl encryptor = null;
		encryptor = new MDEncryptImpl(algorithm.toUpperCase(), size, base64){

			@Override
			public boolean isWithLabel() {
				return withLabel;
			}
			
		};
		return encryptor;
	}
	
	/*public static MDEncrypt getMDEncryptWithBase64(String algorithm){
		if(StringUtils.isBlank(algorithm))
			algorithm = "SHA"+BASE64_POSTFIX;
		else
			algorithm += BASE64_POSTFIX;
		return getMDEncrypt(algorithm);
	}*/
	
	public static MDEncrypt getMDEncrypt(String algorithm){
		if(StringUtils.isBlank(algorithm))
			algorithm = "SHA";
		if(algorithm.startsWith("{") && algorithm.endsWith("}"))
			algorithm = algorithm.substring(1, algorithm.length()-1);
		MDEncrypt encrypt = encrypts.get(algorithm.toUpperCase());
		if(encrypt==null){
			LangUtils.throwBaseException("不支持这个算法：" + algorithm);
		}
		return encrypt;
	}
	
	public static boolean checkEncrypt(String source, String encrypt){
		String label = MDEncryptUtils.getLabel(encrypt, "{md5}");
		MDEncrypt md = getMDEncrypt(label);
		return md.checkEncrypt(source, encrypt);
	}
	
}
