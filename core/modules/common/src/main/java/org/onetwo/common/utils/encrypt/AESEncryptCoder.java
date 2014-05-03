package org.onetwo.common.utils.encrypt;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.onetwo.common.exception.BaseException;

public class AESEncryptCoder extends AbstractEncryptCoder<SecretKey> {

	private static final String AES_KEY = "AES";
	private static final int AES_DEFAULT_LENGTH = 128;
	private static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
//	private final int size = AES_DEFAULT_LENGTH;
	private byte[] key;
	private final int size;
	

	public AESEncryptCoder(){
		this(AES_DEFAULT_LENGTH, true);
	}
	public AESEncryptCoder(boolean generatedSecretKey){
		this(AES_DEFAULT_LENGTH, generatedSecretKey);
	}
	public AESEncryptCoder(int size, boolean generatedSecretKey){
		this.size = size;
		if(generatedSecretKey){
			key = generatedKey().getEncoded();
		}
	}

	public SecretKey generatedKey(){
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
	
	@Override
	protected String getAlgorithmCipher() {
		return AES_CIPHER_ALGORITHM;
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
		return aes(encryptKey, Cipher.ENCRYPT_MODE, byteContent);
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.utils.encrypt.EncryptCoder#dencrypt(byte[])
	 */
	@Override
	public byte[] dencrypt(byte[] dencryptKey, byte[] byteContent){
		return aes(dencryptKey, Cipher.DECRYPT_MODE, byteContent);
	}
	
	private byte[] aes(byte[] key, int opmode, byte[] byteContent){
		//构造密匙
		SecretKeySpec skeySpec = new SecretKeySpec(key, AES_KEY);
//		SecretKeySpec skeySpec = new SecretKeySpec(skeyEncoded, AES_KEY);
		//密码器
		return doCipher(skeySpec, opmode, byteContent);
	}
}
