package org.onetwo.ext.poi.excel.reader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.onetwo.ext.poi.excel.CardEntity;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class WorkbookReaderTest {
	
	@Test
	public void testReader(){
		String path = ExcelUtils.class.getClassLoader().getResource("").getPath()+"/org/onetwo/common/excel/test.xls";
		BeanRowMapper<CardEntity> mapper = new BeanRowMapper<>(CardEntity.class);
		mapper.map("主键", "id");
		mapper.map("卡号", "cardNo");
		mapper.map("密码", "cardPwd");
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(mapper);
		List<CardEntity> cards = reader.readFirstSheet(path);
		assertThat(cards, notNullValue());
		assertThat(cards.size(), is(10));
		assertThat(cards.get(0).getId(), is(1L));
		
		List<CardEntity> cardList = WorkbookReaderFactory.createWorkbookReader(CardEntity.class, 1, 
																			"主键", "id", 
																			"卡号", "cardNo", 
																			"密码", "cardPwd")
														.readFirstSheet(path);
		assertThat(cardList, notNullValue());
		assertThat(cardList.size(), is(10));
		assertThat(cardList.get(0).getId(), is(1L));
	}

	@Test
	public void testReadUnprintChar(){
		String path = "G:/temp/test.xls";
		List<UnprintData> datalist = WorkbookReaderFactory.createWorkbookReader(UnprintData.class, 2, 
																			"序号", "id", 
																			"联系方式", "mobile")
														.readFirstSheet(path);
		assertThat(datalist, notNullValue());
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
