package org.onetwo.common.md;


public interface MessageDigestHasher {
	
	/****
	 * 验证hash
	 * @author wayshall
	 * @param source
	 * @param hashStr hash后的字符串，decode后的bytes包含salt，需分离出salt ，即：hashStr=hash(source+salt)+salt
	 * @return
	 */
	public boolean checkHash(String source, String hashStr);
	/***
	 * 验证hash
	 * @author wayshall
	 * @param source 
	 * @param saltStr
	 * @param hashStr hash后的字符串，decode后的bytes不包含salt，即：hashStr=hash(source+salt)
	 * @return
	 */
	public boolean checkHash(String source, String saltStr, String hashStr);

	public String hashWithSalt(String source, byte[] salt);
	
	public String hashWithSalt(String source, String salt);
	
	public String hash(String source);
	
	/***
	 * 
	 * @author wayshall
	 * @param source
	 * @param length if<1, length=8
	 * @return
	 */
	public String hashWithRandomSalt(String source, int length);
	
//	public String hashWithRandomSalt(String source);
	
	public String hash(byte[] source, byte[] salt);
}
