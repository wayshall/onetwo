package org.onetwo.project.batch.tools.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.excel.AbstractRowMapper;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.onetwo.project.batch.tools.entity.PsamEntity.PsamFactory;
import org.onetwo.project.batch.tools.entity.PsamEntity.PsamStatus;

public class ImportExcelMapper extends AbstractRowMapper<PsamEntity>{

	@Override
	public PsamEntity mapDataRow(List<String> names, Row row, int rowIndex) {
		PsamEntity psam = new PsamEntity();

		psam.setId(Types.convertValue(row.getCell(0).getStringCellValue(), Long.class));
		psam.setPsamNo(row.getCell(1).getStringCellValue());
		psam.setAreaCode(row.getCell(2).getStringCellValue());
		psam.setStartDate(Types.convertValue(row.getCell(3).getStringCellValue(), Date.class));
		psam.setEndDate(Types.convertValue(row.getCell(4).getStringCellValue(), Date.class));
		psam.setStatus(getPsamStatus(row.getCell(5).getStringCellValue()));
		psam.setPsamFactory(getPsamFactory(row.getCell(6).getStringCellValue()));
		return psam;
	}
	
	private PsamStatus getPsamStatus(String name){
		for(PsamStatus pf : PsamStatus.values()){
			if(pf.getName().equals(name))
				return pf;
		}
		throw new BaseException("unknow PsamStatus: " + name);
	}
	
	private PsamFactory getPsamFactory(String name){
		for(PsamFactory pf : PsamFactory.values()){
			if(pf.getLabel().equals(name))
				return pf;
		}
		throw new BaseException("unknow PsamFactory: " + name);
	}

	

}
