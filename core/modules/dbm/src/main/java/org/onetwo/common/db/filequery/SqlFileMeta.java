package org.onetwo.common.db.filequery;

import org.onetwo.common.propconf.ResourceAdapter;

public class SqlFileMeta {
	
	final private ResourceAdapter<?> file;
	final private String postfix;
	public SqlFileMeta(ResourceAdapter<?> file, String postfix) {
		super();
		this.file = file;
		this.postfix = postfix;
	}
	public ResourceAdapter<?> getFile() {
		return file;
	}
	public String getPostfix() {
		return postfix;
	}
	
	

}
