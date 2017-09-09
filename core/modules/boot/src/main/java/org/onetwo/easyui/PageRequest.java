package org.onetwo.easyui;

import lombok.Data;

import org.onetwo.common.utils.Page;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class PageRequest {
	protected int page = 1;
	protected int rows = Page.getDefaultPageSize();
	
	public <E> Page<E> toPageObject(){
		return Page.create(page, rows);
	}

}
