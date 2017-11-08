package org.onetwo.common.encrypt;
/**
 * @author wayshall
 * <br/>
 */
public class Crypts {
	public static final String AES_KEY = "AES";
	
	public static class AESAlgs {
		public static final String ECB_PKCS5Padding = "AES/ECB/PKCS5Padding";
		public static final String CBC_NoPadding = "AES/CBC/NoPadding";
		public static final String CBC_PKCS7Padding = "AES/CBC/PKCS7Padding";
		private AESAlgs(){}
	}
	
	private Crypts(){
	}

}
