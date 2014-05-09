package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Cell;

public interface CellListener {
	
	public void beforeSetValue(Cell cell, Object value);

}
