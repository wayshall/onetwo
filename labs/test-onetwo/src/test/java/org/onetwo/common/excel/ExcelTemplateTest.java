package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.excel.etemplate.ETemplateContext;
import org.onetwo.common.excel.etemplate.ExcelTemplateGenerator;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.NiceDate;

public class ExcelTemplateTest {
	
	public static class LineDataInfo {
		private Long id;
		private String line;
		private String linename;
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
			
			list.add(city);
		}
	}
	
	@Test
	public void testAll(){
		String templateName = "excel-template.xlsx";
		String templatePath = FileUtils.getResourcePath(outputPath)+templateName;
		String generatedPath = FileUtils.getResourcePath(outputPath)+"excel-template-generated.xlsx";
		
		ExcelTemplateGenerator g = new ExcelTemplateGenerator(templatePath);
		ETemplateContext context = new ETemplateContext();
		context.put("year", NiceDate.New().getTime().getYear());
		context.put("now", NiceDate.New());
		context.put("datalist", list);
		g.generate(context, generatedPath);
	}
	

}
