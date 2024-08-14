package org.onetwo.common.encrypt.sm4;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.onetwo.common.encrypt.ByteCoders;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

public class SM4Cipher {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static final SM4Cipher ECB = new SM4Cipher();
    public static final SM4Cipher CBC = new SM4Cipher(SM4PaddingModes.CBC_PADDING);

    // 128-32位16进制；256-64位16进制
    private static final int DEFAULT_KEY_SIZE = 128;
    private int keySize = DEFAULT_KEY_SIZE;
    private String encoding = "UTF-8";
    private String algorithmName = "SM4";
    private SM4PaddingModes paddingModes = SM4PaddingModes.ECB_PADDING;


    public SM4Cipher() {
    }
    
    public SM4Cipher(SM4PaddingModes paddingModes) {
		this.paddingModes = paddingModes;
    }
    
    public SM4Cipher(SM4PaddingModes paddingModes, String encoding) {
		super();
		this.encoding = encoding;
		this.paddingModes = paddingModes;
	}

	public String decrypt(String key, String cipherText, ByteCoders coder) {
    	byte[] bytes = coder.decode(cipherText);
    	byte[] data = decrypt(LangUtils.getBytes(key, encoding), bytes);
    	String strData = LangUtils.newString(data, encoding);
    	return strData;
    }
    
    public byte[] decrypt(byte[] key, byte[] cipherText) {
    	try {
            Cipher cipher = generateCipher(paddingModes, Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherText);
		} catch (Exception e) {
			throw new BaseException("decrypt data error with cbc: " + e.getMessage(), e);
		}
    }
    
    public String encrypt(String key, String plainText, ByteCoders coder) {
    	byte[] rawdata = LangUtils.getBytes(plainText, encoding);
		byte[] result = encrypt(LangUtils.getBytes(key, encoding), rawdata);
		String base64Str =coder.encode(result, encoding);
		return base64Str;
    }
    public byte[] encrypt(byte[] key, byte[] cipherText) {
    	try {
            Cipher cipher = generateCipher(paddingModes, Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(cipherText);
		} catch (Exception e) {
			throw new BaseException("encrypt data error with cbc: " + e.getMessage(), e);
		}
    }

    /**
     * 自动生成密钥
     *
     * @return
     * @explain
     */
    public byte[] generateKey() throws Exception {
        return generateKey(keySize);
    }

    public byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }
    
    private Cipher generateCipher(SM4PaddingModes algorithm, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm.getName(), BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, algorithmName);
        if (algorithm.isECB()) {
        	cipher.init(mode, sm4Key);
        } else {
        	cipher.init(mode, sm4Key, generateIV());
        }
        return cipher;
    }
    

    //生成iv
    private AlgorithmParameters generateIV() {
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params;
		try {
			params = AlgorithmParameters.getInstance(algorithmName);
	        params.init(new IvParameterSpec(iv));
		} catch (NoSuchAlgorithmException | InvalidParameterSpecException e) {
			throw new BaseException("generateIV error: " + e.getMessage(), e);
		}
        return params;
    }

}
