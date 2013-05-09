package org.onetwo.common.spring.rest;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RestUploadVO implements Serializable {

	private String name;
	private byte[] fileDatas;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(byte[] fileDatas) {
		this.fileDatas = fileDatas;
	}

}
