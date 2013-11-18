package org.onetwo.common.utils.encrypt;

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

import org.onetwo.common.exception.BaseException;

public class RSAEncryptCoder extends AbstractEncryptCoder {
	

	private static final String RSA_KEY = "RSA";
	private static final int RSA_DEFAULT_LENGTH = 1024;
	
	private final byte[] publicKey;
	private final byte[] privateKey;
	
	public RSAEncryptCoder(){
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_KEY);
			kpg.initialize(RSA_DEFAULT_LENGTH);
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException("KeyGenerator error: " + e.getMessage() , e);
		}
		
		KeyPair kp = kpg.generateKeyPair();
		
		RSAPublicKey pubkey = (RSAPublicKey)kp.getPublic();
		publicKey = pubkey.getEncoded();
		
		RSAPrivateKey prikey = (RSAPrivateKey)kp.getPrivate();
		privateKey = prikey.getEncoded();
	}

	
	@Override
	protected String getAlgorithmCipher() {
		return RSA_KEY;
	}


	@Override
	public byte[] getKey() {
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
		
		return doCipher(keyPublic, Cipher.ENCRYPT_MODE, byteContent);
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
		return doCipher(keyPrivate, Cipher.DECRYPT_MODE, byteContent);
	}

}
