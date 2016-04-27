package org.onetwo.common.excel.etemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.file.FileUtils;

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
		String templateName = "excel-template.xlsx";
		String templatePath = FileUtils.getResourcePath(outputPath)+templateName;
		String generatedPath = FileUtils.getResourcePath(outputPath)+"excel-template-generated.xlsx";
		System.out.println("generatedPath:"+ generatedPath);
		
		ExcelTemplateEngineer g = new ExcelTemplateEngineer();
		ETemplateContext context = new ETemplateContext();
		context.put("year", NiceDate.New().format("yyyy"));
		context.put("now", NiceDate.New());
		context.put("datalist", list);
		context.put("totalLabel", "合计");
		g.generate(new File(templatePath), context, generatedPath);
	}
	

}
