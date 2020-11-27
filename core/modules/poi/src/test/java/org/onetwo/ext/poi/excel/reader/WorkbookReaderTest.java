package org.onetwo.ext.poi.excel.reader;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.poi.excel.CardEntity;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class WorkbookReaderTest {
	

	@Test
	public void testExcelStreamReaderBuilder(){
		String path = ExcelUtils.class.getClassLoader().getResource("").getPath()+"/org/onetwo/common/excel/test.xls";
		WorkbookReaderFactory.streamReader().readSheet(0).to(1)
										.row(0).onData((dataModel, row, index) -> {
											System.out.println("rwo: " + row.getCell(0));
											assertThat(row.getCellValue(0)).isEqualTo("主键");
										})
										.row(1).toEnd().onData((dataModel, row, index) -> {
											System.out.println("rwo["+index+"]: " + row.getCell(0));
											if (StringUtils.isBlank(row.getString(0))) {
												return ;
											}
											if (row.getSheetIndex()==0) {
												assertThat(row.getCellValue(0)).isEqualTo(String.valueOf(index));
											}
//											PictureData picData = row.getPicture(3);
//											assertThat(picData).isNotNull();
										})
									.endSheet()
								.readSheet(1).to(1)
										.row(0).to(0).onData((dataModel, row, index) -> {
											System.out.println("row2: " + row.getCell(0));
										})
										.row(1).toEnd().onData((dataModel, row, index) -> {
											System.out.println("row2: " + row.getCell(0));
										})
									.endSheet()
								.from(path, null);
		

		path = ExcelUtils.class.getClassLoader().getResource("").getPath()+"/org/onetwo/common/excel/test.xlsx";
		WorkbookReaderFactory.monitorStreamReader().readSheet(0).to(1)
										.row(0).onData((dataModel, row, index) -> {
											System.out.println("rwo: " + row.getCell(0));
											assertThat(row.getCellValue(0)).isEqualTo("主键");
										})
										.row(1).toEnd().onData((dataModel, row, index) -> {
											System.out.println("rwo["+index+"]: " + row.getCell(0));
											if (row.getDouble(0)==null) {
												return ;
											}
											if (row.getSheetIndex()==0) {
												assertThat(row.getCellValue(0)).isEqualTo(String.valueOf(index));
											}
//											PictureData picData = row.getPicture(3);
//											assertThat(picData).isNotNull();
										})
									.endSheet()
								.readSheet(1).to(1)
										.row(0).to(0).onData((dataModel, row, index) -> {
											System.out.println("row2: " + row.getCell(0));
										})
										.row(1).toEnd().onData((dataModel, row, index) -> {
											System.out.println("row2: " + row.getCell(0));
										})
									.endSheet()
								.from(path, null);
	}
	
	@Test
	public void testReader(){
		String path = ExcelUtils.class.getClassLoader().getResource("").getPath()+"/org/onetwo/common/excel/test.xls";
		BeanRowMapper<CardEntity> mapper = new BeanRowMapper<>(CardEntity.class);
		mapper.map("主键", "id");
		mapper.map("卡号", "cardNo");
		mapper.map("密码", "cardPwd");
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(mapper);
		List<CardEntity> cards = reader.readFirstSheet(path);
		assertThat(cards).isNotNull();
		assertThat(cards.size()).isEqualTo(10);
		assertThat(cards.get(0).getId()).isEqualTo(1L);
		
		List<CardEntity> cardList = WorkbookReaderFactory.createWorkbookReader(CardEntity.class, 1, 
																			"主键", "id", 
																			"卡号", "cardNo", 
																			"密码", "cardPwd")
														.readFirstSheet(path); // 读取第一个excel表格
		assertThat(cardList).isNotNull();
		assertThat(cardList.size()).isEqualTo(10);
		assertThat(cardList.get(0).getId()).isEqualTo(1L);
	}

	@Test
	public void testReadUnprintChar(){
		String path = "G:/temp/test.xls";
		List<UnprintData> datalist = WorkbookReaderFactory.createWorkbookReader(UnprintData.class, 2, 
																			"序号", "id", 
																			"联系方式", "mobile")
														.readFirstSheet(path);
		assertThat(datalist).isNotNull();
		for(UnprintData data : datalist){
			//‭18377396337
			int len = (data.getMobile()==null?0:data.getMobile().length());
			System.out.println("data:"+data+", len:"+len);
			if(len>11){
				String newString = data.getMobile().replaceAll("\\p{C}", "?");
				System.out.println("newString:"+newString);
				char[] chars = data.getMobile().toCharArray();
				for (int i = 0; i < chars.length; i++) {
					System.out.println("ch:"+Character.codePointAt(chars, i));
				}
				
				byte[] bytes = data.getMobile().getBytes();
				for (int i = 0; i < bytes.length; i++) {
					System.out.println("bytes:"+bytes[i]);
				}
			}
		}
	}

	public static class UnprintData {
		String id;
		String mobile;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		@Override
		public String toString() {
			return "UnprintData [id=" + id + ", mobile=" + mobile + "]";
		}
		
	}
}
