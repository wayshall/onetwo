package org.onetwo.common.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.jackson.JsonMapper;
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
	
	private Map<String, Object> context;
	
	
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
				card.setProperties(Arrays.asList("aa", "bb"));
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

	@Test
	public void testExport2() {
//		System.out.println("style:"+ReflectUtils.getStaticFieldValue(CellStyle.class, "ALIGN_LEFT"));
		String path = "E:/mydev/ejb3/lvyou2/onetwo-common/test/org/onetwo/common/excel/export_test2.xls";
		PoiExcelGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_test2.xml", context);
		g.generateIt();
		g.write(path);
	}

	@Test
	public void testExportByJsonTemplate() {
//		System.out.println("style:"+ReflectUtils.getStaticFieldValue(CellStyle.class, "ALIGN_LEFT"));
		
		Date startTest = new Date();
		String path = FileUtils.getResourcePath("");
		path += "org/onetwo/common/excel/export_test_json.xls";
		System.out.println("path: " + path);
		
		String templatePath = FileUtils.getResourcePath("org/onetwo/common/excel/jsonTemplate.js");
		String jsonTemplate = FileUtils.readAsString(templatePath);
		WorkbookModel workbook = JsonMapper.IGNORE_EMPTY.fromJson(jsonTemplate, WorkbookModel.class);
		Assert.assertNotNull(workbook);
		TemplateGenerator g = DefaultExcelGeneratorFactory.createWorkbookGenerator(workbook, context);
		g.generateTo(path);
		
		File gfile = new File(path);
		Assert.assertTrue(gfile.exists());
		Assert.assertTrue(gfile.lastModified()>startTest.getTime());
	}

	@Test
	public void testOgnl(){
		Object val = ExcelUtils.getValue("[#data[0].{cardBeans}.{beans}]", context, null);
		System.out.println("val: " + val);
	}
}
