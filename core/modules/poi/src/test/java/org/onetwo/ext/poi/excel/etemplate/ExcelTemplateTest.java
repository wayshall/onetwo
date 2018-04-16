package org.onetwo.ext.poi.excel.etemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.ext.poi.excel.reader.BeanRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.TheFunction;

public class ExcelTemplateTest {
	
	public static class LineDataInfo {
		private Long id;
		private String line;
		private String linename;
		private double money;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}
		public String getLinename() {
			return linename;
		}
		public void setLinename(String linename) {
			this.linename = linename;
		}
		public double getMoney() {
			return money;
		}
		public void setMoney(double money) {
			this.money = money;
		}
		
	}

	private List<LineDataInfo> list;
	private int count = 100;
	
	private String outputPath = "org/onetwo/common/excel/";
	@Before
	public void setup(){
		list = new ArrayList<LineDataInfo>();
		LineDataInfo city;
		for(int i=0; i<count; i++){
			city = new LineDataInfo();
			city.setId(Long.valueOf(i));
			city.setLine("line-"+i);
			city.setLinename("linename"+i);
			city.setMoney(new Random().nextDouble()*10000);
			
			list.add(city);
		}
	}
	
	@Test
	public void testAll(){
//		String templateName = "excel-template.xlsx";
		String templateName = "excel-template.xlsm";
		String templatePath = ExcelUtils.class.getClassLoader().getResource("").getPath()+outputPath+templateName;
		String generatedPath = ExcelUtils.class.getClassLoader().getResource("").getPath()+outputPath+"excel-template-generated.xlsm";
		System.out.println("generatedPath:"+ generatedPath);
		
		ExcelTemplateEngineer g = new ExcelTemplateEngineer();
		g.generate(new File(templatePath), generatedPath, new ETemplateContext(){
			{
				put("year", TheFunction.getInstance().formatDateByPattern("yyyy", new Date()));
				put("now", new Date());
				put("datalist", list);
				put("totalLabel", "合计");
			}
		});
	}
	
	@Test
	public void testDataFromExcel(){
//		String templateName = "excel-template.xlsx";
		BeanRowMapper<HashMap> bm = new BeanRowMapper<>(2, HashMap.class);
		bm.setConvertCellTypeAsString(true);
		List<HashMap> datas = WorkbookReaderFactory.createWorkbookByMapper(bm)
								.readFirstSheet("F:/资料/gf/杂/酒店.xls");
		
		System.out.println("datas:"+datas);
		
		/*ExcelTemplateEngineer g1 = new ExcelTemplateEngineer();
		g1.generate(new File("F:/资料/gf/杂/模板.xls"), "F:/资料/gf/杂/数据-test.xls", new ETemplateContext(){
			{
				put("year", TheFunction.getInstance().formatDateByPattern("yyyy", new Date()));
				put("now", new Date());
			}
		});*/
		
		int index = 0;
		for(HashMap data : datas){
			System.out.println("生成第"+index+"个");
			ExcelTemplateEngineer g = new ExcelTemplateEngineer();
			g.generate(new File("F:/资料/gf/杂/模板.xls"), "F:/资料/gf/杂/数据-"+index+".xls", new ETemplateContext(){
				{
					put("year", TheFunction.getInstance().formatDateByPattern("yyyy", new Date()));
					put("now", new Date());
					putAll(data);
				}
			});
			index++;
		}
	}
	

}
