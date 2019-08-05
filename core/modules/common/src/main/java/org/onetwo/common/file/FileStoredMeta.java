package org.onetwo.common.file;

import java.util.Optional;

public interface FileStoredMeta extends StoredMeta {
	
	/***
	 * 基础路径
	 * @author weishao zeng
	 * @return
	 */
	String getBaseUrl();
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
//	String getStoredServerLocalPath();
	
	
	String getOriginalFilename();
	
	String getBizModule();

	/***
	 * 获取缩略图
	 * @author weishao zeng
	 * @return
	 */
	Optional<StoredMeta> getResizeStoredMeta();
	
	/***
	 * 获取视频截图
	 * @author weishao zeng
	 * @return
	 */
	Optional<StoredMeta> getSnapshotStoredMeta();
	
}
