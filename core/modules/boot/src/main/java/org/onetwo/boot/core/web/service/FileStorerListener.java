package org.onetwo.boot.core.web.service;

import org.onetwo.common.file.FileStoredMeta;

/**
 * @author wayshall
 * <br/>
 */
public interface FileStorerListener {
	
	void afterFileStored(FileStoredMeta meta);

}
