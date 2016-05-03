package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.excel.interfaces.TemplateGenerator;
import org.onetwo.common.excel.utils.ExcelUtils;

import com.google.common.collect.Lists;

public class ExcelExportTest {
	
	public static class CardBeanTool {
		public List<String> getCardPwd(List<Integer> cardNos, List<String> passwords){
			List<String> list = Lists.newArrayList();
			for(int i=0; i<cardNos.size(); i++){
				String str = cardNos.get(i) + "--test--" + passwords.get(i);
				list.add(str);
				if(i==3)
					break;
			}
			return list;
		}
	}
	
	private Map<String, Object> context;
	
	
	@Before
	public void setup(){
		this.context = new HashMap<>();
		int count = 10;
		List<CardEntity> cards = new ArrayList<CardEntity>();
		for(int i=0; i<count; i++){
			CardEntity card = new CardEntity();
			card.setId(Long.valueOf(i+1));
			card.setCardNo("test_card_no_"+i);
			card.setCardPwd("test password in row "+i);
			card.setStartTime(new Date());
			card.setProperties(Arrays.asList("card--aa", "card--bb"));
			
			for(int j=0; j<5; j++){
				CardBean cb = new CardBean();
				cb.setName("cbName-"+i+"-"+j);
				cb.setPassword("pwd-"+j);
				cb.setCardNo(j);
				
				cb.setBeans(Arrays.asList(cb));
				card.addCardBean(cb);
//				card.setProperties(Arrays.asList("aa", "bb"));
			}
			
			cards.add(card);
		}
		this.context.put("data", cards);
		this.context.put("t", new CardBeanTool());
	}

	@Test
	public void testExport() {
		String path = ExcelUtils.getResourcePath("org/onetwo/common/excel/export_test.xls");
//		PoiExcelGenerator g = ExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test.xml", context);
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test.xml", context);
		g.write(path);
	}

	@Test
	public void testExport2() {
//		System.out.println("style:"+ReflectUtils.getStaticFieldValue(CellStyle.class, "ALIGN_LEFT"));
		String path = ExcelUtils.getResourcePath("org/onetwo/common/excel/export_test2.xls");
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test2.xml", context);
		g.write(path);
	}

}
