package org.onetwo.common.utils.encrypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.lang.ArrayUtils;
import org.onetwo.common.exception.BaseException;

public class RSAEncryptCoder extends AbstractEncryptCoder {
	

	private static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/PKCS1PADDING";
//	private static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/NoPadding";
	private static final String RSA_KEY = "RSA";
	private static final int RSA_DEFAULT_LENGTH = 1024;
	
	private final byte[] publicKey;
	private final byte[] privateKey;
	private final int encryptSize;
	private final int dencryptSize;
	

	public RSAEncryptCoder(){
		this(RSA_DEFAULT_LENGTH);
	}
	public RSAEncryptCoder(int size){
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_KEY);
			kpg.initialize(size);
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException("KeyGenerator error: " + e.getMessage() , e);
		}

		this.encryptSize = size/8-11;
		this.dencryptSize = size/8;
		
		KeyPair kp = kpg.generateKeyPair();
		
		RSAPublicKey pubkey = (RSAPublicKey)kp.getPublic();
		publicKey = pubkey.getEncoded();
		
		RSAPrivateKey prikey = (RSAPrivateKey)kp.getPrivate();
		privateKey = prikey.getEncoded();
	}

	
	@Override
	protected String getAlgorithmCipher() {
		return RSA_CIPHER_ALGORITHM;
	}


	@Override
	public byte[] getKey() {
		return publicKey;
	}
	
	public byte[] getPublicKey() {
		return publicKey;
	}
	
	public byte[] getPrivateKey() {
		return privateKey;
	}

	@Override
	public byte[] encrypt(byte[] publicKey, byte[] byteContent) {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf;
		PublicKey keyPublic;
		try {
			kf = KeyFactory.getInstance(RSA_KEY);
			keyPublic = kf.generatePublic(keySpec);
		} catch (Exception e) {
			throw new BaseException("generate public key error: " + e.getMessage() , e);
		}


		byte[] result = chunkedCipher(keyPublic, Cipher.ENCRYPT_MODE, encryptSize, byteContent);
		return result;
	}

	@Override
	public byte[] dencrypt(byte[] privateKey, byte[] byteContent) {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory kf;
		PrivateKey keyPrivate;
		try {
			kf = KeyFactory.getInstance(RSA_KEY);
			keyPrivate = kf.generatePrivate(keySpec);
		} catch (Exception e) {
			throw new BaseException("generate private key error: " + e.getMessage() , e);
		}
		
		byte[] result = chunkedCipher(keyPrivate, Cipher.DECRYPT_MODE, dencryptSize, byteContent);
		return result;
	}
	
	private byte[] chunkedCipher(Key key, int opmode, int chunkSize, byte[] byteContent){
		int chunkCount = byteContent.length/chunkSize;
		chunkCount = (byteContent.length%chunkSize!=0)?(chunkCount+1):chunkCount;
		int chunkStartIndex = 0;
		byte[] temp;
		byte[] result = null;
		for (int i = 0; i < chunkCount; i++) {
			chunkStartIndex = chunkSize*i;
//			if(chunkEndIndex>byteContent.length)
//				chunkEndIndex = byteContent.length;
			temp = doCipher(key, opmode, ArrayUtils.subarray(byteContent, chunkStartIndex, chunkStartIndex+chunkSize));
			System.out.println("temp length: " + temp.length);
			result = ArrayUtils.addAll(result, temp);
		}
		return result;
	}

}
