package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class ExcelBufferReaderTest {

	private Workbook workbook;
	private String path = "card.xlsx";
	
	@Before
	public void init(){
	}

	@Test
	public void testSingleSheet(){
		this.workbook = ExcelUtils.readWorkbookFromClasspath(path);
		BeanRowMapper<CardEntity> beanMapper = new BeanRowMapper<CardEntity>(CardEntity.class);

		LangUtils.println("sheets : ${0}", workbook.getNumberOfSheets());
		LangUtils.println("rowcount : ${0}", (workbook.getSheetAt(0).getPhysicalNumberOfRows()+workbook.getSheetAt(1).getPhysicalNumberOfRows()+workbook.getSheetAt(2).getPhysicalNumberOfRows()));
		int sheetIndex = 0;
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		ExcelBufferReader<CardEntity> reader = new RowMapperSheetBufferReader<CardEntity>(sheet, sheetIndex, beanMapper);
		reader.initReader();
		CardEntity card = null;
		while((card=reader.read())!=null){
			LangUtils.println("cardno: ${0}", card.getCardNo());
			Assert.assertNotNull(card.getCardNo());
		}
		Assert.assertEquals(((RowMapperSheetBufferReader)reader).getRowCount(), 5);
	}
	@Test
	public void testWorkbook(){
		this.workbook = ExcelUtils.readWorkbookFromClasspath(path);
		BeanRowMapper<CardEntity> beanMapper = new BeanRowMapper<CardEntity>(CardEntity.class);

		LangUtils.println("sheets : ${0}", workbook.getNumberOfSheets());
		LangUtils.println("rowcount : ${0}", (workbook.getSheetAt(0).getPhysicalNumberOfRows()+workbook.getSheetAt(1).getPhysicalNumberOfRows()+workbook.getSheetAt(2).getPhysicalNumberOfRows()));
		ExcelBufferReader<CardEntity> reader = new RowMapperWorkbookBufferReader<CardEntity>(workbook, beanMapper);
		reader.initReader();
		CardEntity card = null;
		while((card=reader.read())!=null){
			LangUtils.println("cardno: ${0}", card.getCardNo());
			Assert.assertNotNull(card.getCardNo());
		}
		Assert.assertEquals(((RowMapperWorkbookBufferReader)reader).getRowCount(), 10);
	}

}
