package org.onetwo.common.vo;

import java.io.Serializable;

import org.onetwo.common.utils.Page;

@SuppressWarnings("serial")
public class PageDTO<T> implements Serializable {
	
	private Page<T> page;
	
	public PageDTO(){
		this.page = new Page<T>();
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}
	
}
