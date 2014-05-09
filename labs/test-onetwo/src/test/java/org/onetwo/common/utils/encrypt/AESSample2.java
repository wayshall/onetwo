package org.onetwo.common.utils.encrypt;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/*******************************************************************************
 * 加密/解密
 * 
 * @author LiBaozhen
 * @date May 12, 2011 5:32:06 PM
 * @company LLK
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AESSample2 {
 /**
  * 加密(不支持中文)
  * 
  * @param content
  *            要加密的内容
  * @param key
  *            加密密钥(支持中文密钥)
  * @return
  */
 public static String encrypt(String content, String key) {
  try {
   // 加密
   byte[] encryptByte = aesEncrypt(content, key);
   // 将加密后的二进制字节数转化成16进制
   String encryptResult = parseByte2HexStr(encryptByte);
   return encryptResult;
  } catch (Exception e) {
   return null;
  }
 }
 /**
  * 解密(不支持中文)
  * 
  * @param content
  *            要解密的内容
  * @param key
  *            解密密钥(支持中文密钥)
  * @return
  */

 public static String decrypt(String content, String key) {
  try {
   // 将解密的数据转化成二进制字节数组
   byte[] decryptbyte = parseHexStr2Byte(content);
   // 解密数据
   byte[] decryptResult = aesDecrypt(decryptbyte, key);
   String decryptString = new String(decryptResult);
   return decryptString;
  } catch (Exception e) {
   return null;
  }
 }
 
 /**
  * 测试
  * 
  * @param args
  */
 public static void main(String args[]) {
  String content = "libaozhen@163.com";
  String key = "好阿萨德发射zzidc点发岁的发达省份zz";
  System.out.println(("加密前：" +content));
  System.out.println(("加密后：" +encrypt(content, key)));
    System.out.println(("解密后：" +decrypt(encrypt(content, key), key)));
   }
 
 /**
  * 将二进制转换成16进制
  * 
  * @param buf
  *            二进制字节数组
  * @return
  */
 private static String parseByte2HexStr(byte buf[]) {
  StringBuffer sb = new StringBuffer();
  for (int i = 0; i < buf.length; i++) {
   String hex = Integer.toHexString(buf[i] & 0xFF);
   if (hex.length() == 1) {
    hex = '0' + hex;
   }
   sb.append(hex.toUpperCase());
  }
  return sb.toString();
 }
 
 /**
  * 将16进制转换为二进制
  * 
  * @param hexStr
  *            16进制字符串
  * @return
  */
 private static byte[] parseHexStr2Byte(String hexStr) {
  if (hexStr.length() < 1)
   return null;
  byte[] result = new byte[hexStr.length() / 2];
  for (int i = 0; i < hexStr.length() / 2; i++) {
   int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
   int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
     16);
   result[i] = (byte) (high * 16 + low);
  }
  return result;
 }
 
 /**
  * AES加密
  * 
  * @param content
  *            需要加密的内容
  * @param key
  *            加密密钥
  * @return
  */
 private static byte[] aesEncrypt(String content, String key) {
  try {
   // 返回生成指定算法的私密密钥的KeyGenerator对象
   KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
   // 使用用户提供的随机源(key)初始化此密钥生成器,生成192位
   keyGenerator.init(128, new SecureRandom(key.getBytes("utf-8"))); // 初始化
   // 生成一个密钥
   SecretKey secretKey = keyGenerator.generateKey();
   // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
   byte[] enCodeFormat = secretKey.getEncoded();
   // 根据给定的字节数组和算法构造一个密钥
   SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
   // 返回实现指定转换的 Cipher 对象
   Cipher cipher = Cipher.getInstance("AES");// 创建密码器
   byte[] byteContent = content.getBytes("utf-8");
   // Cipher.ENRYPT_MODE 将 Cipher 初始化为加密模式的常量
   cipher.init(Cipher.ENCRYPT_MODE, seckey); // 初始化
   // 按单部分操作加密数据，或者结束一个多部分操作
   byte[] result = cipher.doFinal(byteContent); // 加密
   return result;
  } catch (Exception e) {
   e.printStackTrace();
  }
  return null;
 }
 
 /**
  * AES解密
  * 
  * @param content
  *            待解密内容
  * @param key
  *            解密密钥
  * @return
  */
 private static byte[] aesDecrypt(byte[] content, String key) {
  try {
   // 返回生成指定算法的私密密钥的KeyGenerator对象
   KeyGenerator keyGenerator = KeyGenerator.getInstance("AES"); 
   // 使用用户提供的随机源(key)初始化此密钥生成器,生成192位
   keyGenerator.init(128, new SecureRandom(key.getBytes("utf-8"))); 
   // 生成一个密钥
   SecretKey secretKey = keyGenerator.generateKey();
   // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
   byte[] enCodeFormat = secretKey.getEncoded();
   // 根据给定的字节数组和算法构造一个密钥
   SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
   // 返回实现指定转换的 Cipher 对象
   Cipher cipher = Cipher.getInstance("AES");// 创建密码器
   // Cipher.DECRYPT_MODE 将 Cipher 初始化为解密模式的常量
   cipher.init(Cipher.DECRYPT_MODE, seckey);
   // 按单部分操作解密数据，或者结束一个多部分操作
   byte[] result = cipher.doFinal(content);
   return result; // 加密
  } catch (Exception e) {
   e.printStackTrace();
  }
  return null;
 }
}