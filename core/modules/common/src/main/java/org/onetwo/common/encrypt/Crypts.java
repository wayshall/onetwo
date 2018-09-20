package org.onetwo.common.encrypt;
/**
 * @author wayshall
 * <br/>
 */
public class Crypts {
	public static final String AES_KEY = "AES";
	
	public static class AESAlgs {
		public static final String ECB_PKCS5Padding = "AES/ECB/PKCS5Padding";
		/***
		 * aes算法的block size是128位(16 byte)的
		 * 如果使用NoPadding算法，必须要自己padding 128 的倍数
		 */
		public static final String CBC_NoPadding = "AES/CBC/NoPadding";
		public static final String CBC_PKCS7Padding = "AES/CBC/PKCS7Padding";
		public static final String CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
		private AESAlgs(){}
	}
	
	private Crypts(){
	}

}
