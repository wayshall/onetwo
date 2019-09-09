package org.onetwo.common.file;
/**
 * @author weishao zeng
 * <br/>
 */
public interface StoredMeta {

	
	/***
	 * 上传成功后客户端应该保存此路径到数据库
	 * accessable path，exclude host: /aa/bb/cc.jpg
	 * @author wayshall
	 * @return
	 */
	String getAccessablePath();
	/***
	 * 保存后的文件名
	 * @author wayshall
	 * @return
	 */
	String getSotredFileName();
}

