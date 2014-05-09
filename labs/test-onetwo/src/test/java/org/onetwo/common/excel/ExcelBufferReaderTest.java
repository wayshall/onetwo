package org.onetwo.common.excel;

import java.util.Arrays;
import java.util.List;

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
	public void testTitleMapper(){
		String path = "card_index.xlsx";
//		this.workbook = ExcelUtils.readWorkbookFromClasspath(path);
		BeanRowMapper<CardEntity> beanMapper = BeanRowMapper.map(CardEntity.class, 0, "cardNo", 1, "userName", 2, "cardPwd", 3, "cardType.id", 4, "cardType.cardBean.cardNo");

		List<String> cardNos = Arrays.asList("111111", "222222", "33333", "44444");
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(beanMapper);
		List<CardEntity> cardList = reader.readFirstSheet(path);
		Assert.assertEquals(4, cardList.size());
		int rowIndex = 0;
		for(CardEntity card : cardList){
			Assert.assertEquals(cardNos.get(rowIndex), card.getCardNo());
			rowIndex++;
		}
	}

	@Test
	public void testTitleMapperByName(){
		String path = "card_cnname.xlsx";
//		this.workbook = ExcelUtils.readWorkbookFromClasspath(path);
		BeanRowMapper<CardEntity> beanMapper = BeanRowMapper.map(CardEntity.class, "卡号", "cardNo", "姓名", "userName", "密码", "cardPwd", "类型", "cardType.id", "卡号2", "cardType.cardBean.cardNo");

		List<String> cardNos = Arrays.asList("111111", "222222", "33333", "44444");
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(beanMapper);
		List<CardEntity> cardList = reader.readFirstSheet(path);
		Assert.assertEquals(4, cardList.size());
		int rowIndex = 0;
		for(CardEntity card : cardList){
			Assert.assertEquals(cardNos.get(rowIndex), card.getCardNo());
			rowIndex++;
		}
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
