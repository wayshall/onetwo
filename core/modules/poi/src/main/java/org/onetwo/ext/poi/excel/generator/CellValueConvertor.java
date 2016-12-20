package org.onetwo.ext.poi.excel.generator;

import org.apache.poi.ss.usermodel.Cell;

public interface CellValueConvertor {

	public Object convert(Cell cell);
}
