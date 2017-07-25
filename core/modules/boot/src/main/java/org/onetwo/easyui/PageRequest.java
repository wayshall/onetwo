package org.onetwo.easyui;

import lombok.Data;

import org.onetwo.common.utils.Page;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class PageRequest {
	private int page = 1;
	private int rows = 20;
	private boolean pagination = true;
	
	public <E> Page<E> toPageObject(){
		Page<E> p = Page.create(page, rows);
		p.setPagination(pagination);
		return p;
	}

}
