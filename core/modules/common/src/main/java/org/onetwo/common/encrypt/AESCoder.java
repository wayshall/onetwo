package org.onetwo.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.onetwo.common.encrypt.Crypts.AESAlgs;
import org.onetwo.common.exception.BaseException;


/**
 * @author wayshall
 * <br/>
 */
public class AESCoder {
	
	public static AESCoder pkcs7Padding(byte[] aesKey){
		return new AESCoder(AESAlgs.CBC_PKCS7Padding, aesKey);
	}
	
	final protected String cipher;
	final protected byte[] aesKey;
	private CipherIniter initer;
	
	public AESCoder(String cipher, byte[] aesKey) {
		super();
		this.cipher = cipher;
		this.aesKey = aesKey;
	}
	
	public AESCoder initer(CipherIniter initer) {
		this.initer = initer;
		return this;
	}

	public byte[] encrypt(byte[] ecryptData){
		return doFinal(Cipher.ENCRYPT_MODE, ecryptData);
	}
	
	public byte[] decrypt(byte[] ecryptData){
		return doFinal(Cipher.DECRYPT_MODE, ecryptData);
	}
	
	protected byte[] doFinal(int mode, byte[] ecryptData){
		Cipher cipher = createCipher(mode);
		byte[] res;
		try {
			res = cipher.doFinal(ecryptData);
		} catch (Exception e) {
			throw new BaseException("decrypt error: " + e.getMessage(), e);
		} 
		return res;
	}
	
	public Cipher createCipher(int mode){
		try {
			Cipher cipherInst = Cipher.getInstance(cipher);
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, Crypts.AES_KEY);
			this.init(cipherInst, mode, keySpec);
			return cipherInst;
		} catch (Exception e) {
			throw new BaseException("create Cipher error: " + e.getMessage(), e);
		}
	}
	
	protected void init(Cipher cipherInst, int mode, SecretKeySpec keySpec) throws Exception {
		if(this.initer!=null){
			this.initer.init(cipherInst, mode, keySpec);
		}else{
			cipherInst.init(mode, keySpec);
		}
		/*if(isCbc()){
			IvParameterSpec iv = null;
			if(ivKey!=null){
				iv = new IvParameterSpec(ivKey);
			}else{
				iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			}
			cipherInst.init(mode, key_spec, iv);
		}else{
			cipherInst.init(mode, key_spec);
		}*/
	}
	
	public static interface CipherIniter {
		void init(Cipher cipherInst, int mode, SecretKeySpec keySpec) throws Exception ;
	}
	
}
