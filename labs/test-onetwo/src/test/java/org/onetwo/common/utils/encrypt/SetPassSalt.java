package org.onetwo.common.utils.encrypt;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.onetwo.common.utils.StringUtils;

public class SetPassSalt {

	/**
	 * @param args
	 */
	public static String calcMD5(String source, String saltStr) {
        //生成盐 
//        Random     rand=new   Random(); 
//        rand.nextBytes(salt); 
        //计算消息摘要 
        MessageDigest m;
        String   result= ""; 
		try {
//			m = MessageDigest.getInstance("MD5");
			m = MessageDigest.getInstance("SHA");
	        m.update(source.getBytes()); 

			if(StringUtils.isNotBlank(saltStr)){
		        byte[] salt=saltStr.getBytes(); 
		        m.update(salt); 
			}
	        byte s[]=m.digest(); 
	        for(int i=0; i <s.length;i++){ 
	           result+=Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6); 
	        } 
//	        result = new String(Base64.encodeBase64(s));
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return result.toUpperCase();
	}

}
