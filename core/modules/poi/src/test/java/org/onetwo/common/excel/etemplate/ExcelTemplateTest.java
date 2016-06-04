package org.onetwo.common.excel.etemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.onetwo.common.excel.utils.TheFunction;

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
	

}
