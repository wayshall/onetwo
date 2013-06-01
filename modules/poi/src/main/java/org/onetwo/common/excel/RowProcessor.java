package org.onetwo.common.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public interface RowProcessor {
	public void processRow(HSSFSheet sheet, RowModel rowModel);
}
