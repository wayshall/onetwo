package org.onetwo.ext.poi.excel.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class ListRowMapper extends AbstractRowMapper<List<Object>> {
	
	public static class StringListRowMapper extends ListRowMapper {

		@Override
		protected Object getCellValue(Cell cell){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
	}

	public ListRowMapper() {
		super(WorkbookReaderFactory.convertors);
	}
	
	@Override
	public List<String> mapTitleRow(Sheet sheet) {
		return null;
	}

	@Override
	public int getDataRowStartIndex() {
		return 0;
	}

	@Override
	public List<Object> mapDataRow(List<String> names, Row row, int rowIndex) {
		int cellCount = row.getPhysicalNumberOfCells();

		List<Object> list = new ArrayList<Object>();
		Object val = null;
		for (int i = 0; i < cellCount; i++) {
			Cell cell = row.getCell(i);
			if(cell==null)
				continue;
			val = getCellValue(cell);
			list.add(val);
		}
		return list;
	}
	
	protected Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}


}
