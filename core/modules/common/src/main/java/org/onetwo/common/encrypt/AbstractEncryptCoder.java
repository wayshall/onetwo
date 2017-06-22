package org.onetwo.common.encrypt;

import java.security.Key;

import javax.crypto.Cipher;

import org.onetwo.common.exception.BaseException;

abstract public class AbstractEncryptCoder<T> implements EncryptCoder<T>{

	abstract protected String getAlgorithmCipher();
	

	protected byte[] doCipher(Key skeySpec, int opmode, byte[] byteContent){
		//构造密匙
//		SecretKeySpec skeySpec = new SecretKeySpec(key, AES_KEY);
//		SecretKeySpec skeySpec = new SecretKeySpec(skeyEncoded, AES_KEY);
		//密码器
		Cipher aesCipher = null;
		byte[] result = null;
		try{
			aesCipher = Cipher.getInstance(getAlgorithmCipher());
			init(aesCipher, opmode, skeySpec);
			result = aesCipher.doFinal(byteContent);
		}catch (Exception e) {
			throw new BaseException("Cipher error: " + e.getMessage() , e);
		}
		return result;
	}
	
	protected void init(Cipher cipher, int opmode, Key skeySpec) throws Exception{
		cipher.init(opmode, skeySpec);
	}
}
