package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValueConvertor {

	public Object convert(Cell cell);
}
