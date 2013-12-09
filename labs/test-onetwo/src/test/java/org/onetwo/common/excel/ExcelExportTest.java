package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;

public class ExcelExportTest {
	
	public static class CardBeanTool {
		public List<String> getCardPwd(List<Integer> cardNos, List<String> passwords){
			List<String> list = LangUtils.newArrayList();
			for(int i=0; i<cardNos.size(); i++){
				String str = cardNos.get(i) + "--test--" + passwords.get(i);
				list.add(str);
				if(i==3)
					break;
			}
			return list;
		}
	}
	
	private Map context;
	
	
	@Before
	public void setup(){
		this.context = new HashMap();
		int count = 10;
		List<CardEntity> cards = new ArrayList<CardEntity>();
		for(int i=0; i<count; i++){
			CardEntity card = new CardEntity();
			card.setId(Long.valueOf(i+1));
			card.setCardNo("test_card_no_"+i);
			card.setCardPwd("testPwd");
			card.setStartTime(DateUtil.now());
			
			for(int j=0; j<5; j++){
				CardBean cb = new CardBean();
				cb.setName("cbName-"+i+"-"+j);
				cb.setPassword("pwd-"+j);
				cb.setCardNo(j);
				
				cb.setBeans(JFishList.wrap(cb));
				card.addCardBean(cb);
			}
			
			cards.add(card);
		}
		this.context.put("data", cards);
		this.context.put("t", new CardBeanTool());
	}

	@Test
	public void testExport() {
		String path = FileUtils.getResourcePath("org/onetwo/common/excel/export_test.xls");
//		PoiExcelGenerator g = ExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test.xml", context);
		PoiExcelGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test.xml", context);
		g.generateIt();
		g.write(path);
	}

//	@Test
	public void testExport2() {
//		System.out.println("style:"+ReflectUtils.getStaticFieldValue(CellStyle.class, "ALIGN_LEFT"));
		String path = "E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/excel/export_test2.xls";
		PoiExcelGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test2.xml", context);
		g.generateIt();
		g.write(path);
	}

	@Test
	public void testOgnl(){
		Object val = ExcelUtils.getValue("[#data[0].{cardBeans}.{beans}]", context, null);
		System.out.println("val: " + val);
	}
}
