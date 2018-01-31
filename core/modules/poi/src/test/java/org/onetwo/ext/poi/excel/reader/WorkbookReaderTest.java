package org.onetwo.ext.poi.excel.reader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.onetwo.ext.poi.excel.CardEntity;
import org.onetwo.ext.poi.excel.reader.BeanRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReader;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;
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

}
