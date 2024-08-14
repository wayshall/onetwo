package org.onetwo.common.encrypt.sm4;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.onetwo.common.encrypt.ByteCoders;

public class Sm4Util {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ENCODING = "UTF-8";

    public static final String ALGORITHM_NAME = "SM4"; //BaseSmEnum.ALGORITHM_NAME.getMsg();
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding

//    public static final String ALGORITHM_NAME_CBC_PADDING = SM4PaddingModes.CBC_PADDING.getMsg();
//    public static final String ALGORITHM_NAME_CBC_PADDING = SM4PaddingModes.ECB_PADDING.getMsg();
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;
    

    public static String decryptBase64WithECBPadding(String key, String encryptText) {
//    	byte[] rawdata = Base64.decodeBase64(encryptText);
//		byte[] result = decryptECBPadding(LangUtils.getBytes(key), rawdata);
//		String decryptText = LangUtils.newString(result, ENCODING);
//		return decryptText;
    	
    	String originText = SM4Cipher.ECB.decrypt(key, encryptText, ByteCoders.BASE64);
    	return originText;
    }

    public static String encryptBase64WithECBPadding(String key, String plainText) {
//    	byte[] rawdata = LangUtils.getBytes(plainText);
//		byte[] result = encryptECBPadding(LangUtils.getBytes(key), rawdata);
//		byte[] base64Bytes = Base64.encodeBase64(result);
//		String base64Str = LangUtils.newString(base64Bytes, ENCODING);
//		return base64Str;
    	
    	String base64Str = SM4Cipher.ECB.encrypt(key, plainText, ByteCoders.BASE64);
    	return base64Str;
    }

    /**
     * 自动生成密钥
     *
     * @return
     * @explain
     */
//    public static byte[] generateKey() throws Exception {
//        return generateKey(DEFAULT_KEY_SIZE);
//    }


//    public static String generateKeyString() throws Exception {
//        return ByteUtils.toHexString(generateKey());
//    }

    /**
     * @param keySize
     * @return
     * @throws Exception
     * @explain
     */
//    public static byte[] generateKey(int keySize) throws Exception {
//        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
//        kg.init(keySize, new SecureRandom());
//        return kg.generateKey().getEncoded();
//    }

    /**
     * sm4加密
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     * @explain 加密模式：CBC
     */
//    public static String protectMsg(String hexKey, String paramStr) throws Exception {
//        String result = "";
//        // 16进制字符串-->byte[]
//        byte[] keyData = ByteUtils.fromHexString(hexKey);
//        // String-->byte[]
//        byte[] srcData = paramStr.getBytes(ENCODING);
//        // 加密后的数组
//        byte[] cipherArray = encryptPadding(keyData, srcData);
//
//        // byte[]-->hexString
//        result = ByteUtils.toHexString(cipherArray);
//        return result;
//    }

    /**
     * 加密模式之CBC
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     * @explain
     */
//    public static byte[] encryptPadding(byte[] key, byte[] data) throws Exception {
//        Cipher cipher = generateCipher(SM4PaddingModes.CBC_PADDING, Cipher.ENCRYPT_MODE, key);
//        return cipher.doFinal(data);
//    }

//    private static Cipher generateCipher(SM4PaddingModes algorithm, int mode, byte[] key) throws Exception {
//        Cipher cipher = Cipher.getInstance(algorithm.getName(), BouncyCastleProvider.PROVIDER_NAME);
//        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
//        if (algorithm.isECB()) {
//        	cipher.init(mode, sm4Key);
//        } else {
//        	cipher.init(mode, sm4Key, generateIV());
//        }
//        return cipher;
//    }

    //生成iv
//    public static AlgorithmParameters generateIV() throws Exception {
//        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
//        byte[] iv = new byte[16];
//        Arrays.fill(iv, (byte) 0x00);
//        AlgorithmParameters params = AlgorithmParameters.getInstance(ALGORITHM_NAME);
//        params.init(new IvParameterSpec(iv));
//        return params;
//    }

    /**
     * sm4解密
     *
     * @param hexKey 16进制密钥
     * @param text   16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception
     * @explain 解密模式：采用CBC
     */
//    public static String uncoverMsg(String hexKey, String text) throws Exception {
//        // 用于接收解密后的字符串
//        String result = "";
//        // hexString-->byte[]
//        byte[] keyData = ByteUtils.fromHexString(hexKey);
//        // hexString-->byte[]
//        byte[] resultData = ByteUtils.fromHexString(text);
//        // 解密
//        byte[] srcData = decryptCbcPadding(keyData, resultData);
//        // byte[]-->String
//        result = new String(srcData, ENCODING);
//        return result;
//    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     * @explain
     */
//    public static byte[] decryptCbcPadding(byte[] key, byte[] cipherText) {
//    	try {
//            Cipher cipher = generateCipher(SM4PaddingModes.CBC_PADDING, Cipher.DECRYPT_MODE, key);
//            return cipher.doFinal(cipherText);
//		} catch (Exception e) {
//			throw new BaseException("decrypt data error with cbc: " + e.getMessage(), e);
//		}
//    }
//    public static byte[] decryptECBPadding(byte[] key, byte[] cipherText) {
//    	try {
//            Cipher cipher = generateCipher(SM4PaddingModes.ECB_PADDING, Cipher.DECRYPT_MODE, key);
//            return cipher.doFinal(cipherText);
//		} catch (Exception e) {
//			throw new BaseException("decrypt data error with cbc: " + e.getMessage(), e);
//		}
//    }
    
//    public static byte[] encryptECBPadding(byte[] key, byte[] cipherText) {
//    	try {
//            Cipher cipher = SM4Cipher.INSTANCE.generateCipher(SM4PaddingModes.ECB_PADDING, Cipher.ENCRYPT_MODE, key);
//            return cipher.doFinal(cipherText);
//		} catch (Exception e) {
//			throw new BaseException("encrypt data error with cbc: " + e.getMessage(), e);
//		}
//    }

    
}