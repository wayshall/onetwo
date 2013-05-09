package org.onetwo.common.excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ListRowMapper extends AbstractRowMapper<Object[]> {
	
	public static class StringListRowMapper extends ListRowMapper {

		@Override
		protected Object getCellValue(Cell cell){
			Object obj = super.getCellValue(cell);
			if(Number.class.isInstance(obj)){
				obj = new BigDecimal(obj.toString());
			}
			return obj.toString();
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
	public Object[] mapDataRow(List<String> names, Row row, int rowIndex) {
		int cellCount = row.getPhysicalNumberOfCells();

		List<Object> list = new ArrayList<Object>();
		Object val = null;
		for (int i = 0; i < cellCount; i++) {
			Cell cell = row.getCell(i);
			val = getCellValue(cell);
			list.add(val);
		}
		return list.toArray();
	}
	
	protected Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}


}
