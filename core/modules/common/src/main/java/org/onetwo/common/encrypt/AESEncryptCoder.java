package org.onetwo.common.encrypt;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.onetwo.common.exception.BaseException;

public class AESEncryptCoder implements EncryptCoder<SecretKey> {

	private static final String AES_KEY = "AES";
	public static final int AES_DEFAULT_LENGTH = 128;
	private static final String AES_ECB_ALGORITHM = "AES/ECB/PKCS5Padding";
	public static final String AES_CBC_ALGORITHM = "AES/CBC/NoPadding";
	
//	private final int size = AES_DEFAULT_LENGTH;
	private byte[] key;
//	private final int size;
	private String cipher = AES_ECB_ALGORITHM;
	

	public AESEncryptCoder(){
		this(true);
	}
	public AESEncryptCoder(boolean generatedSecretKey){
		if(generatedSecretKey){
			key = generatedKey().getEncoded();
		}
	}
	
	public AESEncryptCoder(String cipher, byte[] key) {
		super();
		this.cipher = cipher;
		this.key = key;
	}

	public SecretKey generatedKey(){
		return generatedKey(AES_DEFAULT_LENGTH);
	}
	
	public SecretKey generatedKey(int size){
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(AES_KEY);
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException("KeyGenerator error: " + e.getMessage() , e);
		}
		keyGenerator.init(size);
//		keyGenerator.init(length);
		SecretKey skey = keyGenerator.generateKey();
		return skey;
	}
	
	protected String getAlgorithmCipher() {
		return cipher;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.encrypt.EncryptCoder#getKey()
	 */
	@Override
	public byte[] getKey(){
		return key;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.encrypt.EncryptCoder#encrypt(byte[])
	 */
	@Override
	public byte[] encrypt(byte[] encryptKey, byte[] byteContent){
		return doCipher(encryptKey, Cipher.ENCRYPT_MODE, byteContent);
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.encrypt.EncryptCoder#dencrypt(byte[])
	 */
	@Override
	public byte[] dencrypt(byte[] dencryptKey, byte[] byteContent){
		return doCipher(dencryptKey, Cipher.DECRYPT_MODE, byteContent);
	}

	protected void init(Cipher cipher, byte[] key, int opmode) throws Exception{
		//构造密匙
		SecretKeySpec skeySpec = new SecretKeySpec(key, AES_KEY);
		cipher.init(opmode, skeySpec);
		/*if(AES_CBC_ALGORITHM.equalsIgnoreCase(getAlgorithmCipher())){
			IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
			cipher.init(opmode, skeySpec, iv);
		}else{
			cipher.init(opmode, skeySpec);
		}*/
	}
	
	protected byte[] doCipher(byte[] key, int opmode, byte[] byteContent){
		//密码器
		Cipher aesCipher = null;
		byte[] result = null;
		try{
			aesCipher = Cipher.getInstance(getAlgorithmCipher());
			init(aesCipher, key, opmode);
			result = aesCipher.doFinal(byteContent);
		}catch (Exception e) {
			throw new BaseException("Cipher error: " + e.getMessage() , e);
		}
		return result;
	}
	
}
