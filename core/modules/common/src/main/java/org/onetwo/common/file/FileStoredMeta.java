package org.onetwo.common.file;


public interface FileStoredMeta {
	
	/***
	 * 上传成功后客户端应该保存此路径到数据库
	 * accessable path，exclude host: /aa/bb/cc.jpg
	 * @author wayshall
	 * @return
	 */
	String getAccessablePath();
	/***
	 * 包含了图片服务器地址的路径
	 * full accessable path, include host : http://host.com/img/aa/bb/cc.jpg
	 * @author wayshall
	 * @return
	 */
	String getFullAccessablePath();
	
	/***
	 * 存储服务器的本地保存路径
	 * 如果是ftp，则是ftp保存的路径
	 * @author wayshall
	 * @return
	 */
	String getStoredServerLocalPath();
	
	/***
	 * 保存后的文件名
	 * @author wayshall
	 * @return
	 */
	String getSotredFileName();
	
	String getOriginalFilename();
	
	String getBizModule();
	
	
//	public File toFile();
	
}
