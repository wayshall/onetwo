package org.onetwo.plugins.batch;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.excel.SSFRowMapper;
import org.onetwo.common.utils.StringUtils;

public class SimpleExcelMapper implements SSFRowMapper<BoxNumberEntity>{
	
	public String getMapperName() {
		return "row";
	}

	public List<String> mapTitleRow(int sheetIndex, Sheet sheet) {
		Row titleRow = sheet.getRow(0);
		return ExcelUtils.getRowValues(titleRow);
	}

	public int getDataRowStartIndex() {
		return 1;
	}

	public BoxNumberEntity mapDataRow(Sheet sheet, List<String> names, Row row, int rowIndex) {
		if(StringUtils.isBlank(row.getCell(1).getStringCellValue()))
			return null;
		BoxNumberEntity box=null;
		box=new BoxNumberEntity();
		box.setDataBatch(row.getCell(1).getStringCellValue());
	    box.setCountryCode(row.getCell(2).getStringCellValue());
	    box.setCountryName(row.getCell(3).getStringCellValue());
	    box.setTownCode(row.getCell(4).getStringCellValue());
	    box.setTownName(row.getCell(5).getStringCellValue());
	    box.setVillageCode(row.getCell(6).getStringCellValue());
	    box.setVillageName(row.getCell(7).getStringCellValue());
	    box.setBbox(row.getCell(8).getStringCellValue());
	    box.setBox(row.getCell(9).getStringCellValue());
	    box.setAddress(row.getCell(11).getStringCellValue());
	    box.setName(row.getCell(12).getStringCellValue());
	    box.setCertCode(row.getCell(13).getStringCellValue());
		return box;
	}
	
}
