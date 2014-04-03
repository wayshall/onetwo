package org.onetwo.common.excel;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.interfaces.TemplateGenerator;
import org.onetwo.common.profiling.Timeit;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.FileUtils;

public class ExcelTest {
//	private WorkbookGeneratorFactory wf = new WorkbookGeneratorFactory();
	
	private List<CityCompainInfo> list;
	private int count = 100;
	
	private String outputPath = "org/onetwo/common/excel/";
	@Before
	public void setup(){
		list = new ArrayList<CityCompainInfo>();
		CityCompainInfo city;
		for(int i=0; i<count; i++){
			city = new CityCompainInfo();
			city.setId(Long.valueOf(i));
			city.setName("name"+i);
			city.setPhone("phone"+i);
			city.setPostcode("postcode"+i);
			city.setAddress("address"+i);
			city.setFax("fax"+i);
			city.setFaxType(i%3);
			
			list.add(city);
		}
	}
	
	@Test
	public void testAll(){
//		this.testTemplateWithReflect();
//		this.testTemplateWithReflectOgnl();
		
//		UtilTimerStack.setActive(true);
//		UtilTimerStack.setNanoTime(true);
		Timeit.create()
		.timeit(this, "testRawPoi")
		.timeit(this, "testTemplateWithReflectOgnl")
		.timeit(this, "testTemplateWithReflect")
		.timeit(this, "testTemplateWithOgnl")
		.printAll();
	}
	
//	@Test
	public void testRawPoi(){
//		UtilTimerStack.setActive(true);
		String[] titles = new String[]{"主键", "名称", "电话", "邮编", "地址", "传真"};
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("报名客户资料");
		int rownum = 0;
		HSSFRow row = sheet.createRow(rownum++);
		HSSFCell cell = null;
		for(int i=0; i<titles.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(titles[i]);
		}
		
		String colName = "";
		int profileCount = 50;
		for(CityCompainInfo city : list){
			int column = 0;
			if(column<profileCount){
				colName = "row-"+column;
				UtilTimerStack.push(colName);
			}
			
			row = sheet.createRow(rownum++);
			//id
			cell = row.createCell(column++);
			cell.setCellValue(city.getId());
			//name
			cell = row.createCell(column++);
			cell.setCellValue(city.getName());
			//phone
			cell = row.createCell(column++);
			cell.setCellValue(city.getPhone());
			//postcode
			cell = row.createCell(column++);
			cell.setCellValue(city.getPostcode());
			//address
			cell = row.createCell(column++);
			cell.setCellValue(city.getAddress());
			//fax
			cell = row.createCell(column++);
			cell.setCellValue(city.getFax());
			

			if(column<profileCount){
				UtilTimerStack.pop(colName);
			}
		}

		String path = FileUtils.getResourcePath(outputPath)+"city_company_raw.xls";
		try {
			workbook.write(new FileOutputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("raw path: " + path);
	}
	
	@Test
	public void testMutilSheetTemplate(){
		System.out.println("===========================>>>>>testTemplateWithMutilSheet ");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("data", list);
		String path = FileUtils.getResourcePath(outputPath)+"export_multi_sheets_test.xls";
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_multi_sheets_test.xml", context);
		g.generateIt();
		g.write(path);
	}
	@Test
	public void testMutilSheetTemplateWithExportDatasource(){
		System.out.println("===========================>>>>>testTemplateWithMutilSheet ");
		Map<String, Object> context = new HashMap<String, Object>();
		final List<?> datalist = list;
		context.put("data", new ExportDatasource() {
			
			@Override
			public List<?> getSheetDataList(int i) {
				int toIndex = 10*(i+1);
				if(toIndex>datalist.size())
					toIndex = datalist.size();
				
				return datalist.subList(10*i, toIndex);
			}

			@Override
			public String getSheetLabel(int sheetIndex) {
				return "报表" + sheetIndex;
			}
			
		});
		String path = FileUtils.getResourcePath(outputPath)+"export_multi_sheets_with_exportds.xls";
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_multi_sheets_with_exportds.xml", context);
		g.generateIt();
		g.write(path);
	}
	
	@Test
	public void testExportMutilSheetWithWorkbook(){
		System.out.println("===========================>>>>>testExportMutilSheetWithWorkbook ");
		Map<String, Object> context = new HashMap<String, Object>();
		final List<?> datalist = list;
		final int pageSize = 30;
		context.put("data", new PageExportDatasource() {
			
			@Override
			public int getTotalPages() {
				return datalist.size()%pageSize==0?(datalist.size()/pageSize):(datalist.size()/pageSize+1);
			}

			@Override
			public List<?> getSheetDataList(int i) {
				int fromIndex = pageSize*i;
				if(fromIndex>datalist.size())
					return null;
				int toIndex = pageSize*(i+1);
				if(toIndex>datalist.size())
					toIndex = datalist.size();
				
				return datalist.subList(fromIndex, toIndex);
			}

			@Override
			public String getSheetLabel(int sheetIndex) {
				return "报表" + sheetIndex;
			}
			
		});
//		context.put("sheet2", datalist.subList(count/2, count));sheet和xml的template名称冲突
		context.put("data2", datalist.subList(count/2, count));
		context.put("listener", new EmptyWorkbookListener(){

			@Override
			public void afterCreateCell(Cell cell, int cellIndex) {
//				LangUtils.println("afterCreateCell ${0} ", cellIndex);
			}
			
		});
		context.put("sumField", new SumFieldValueExecutor());
		String path = FileUtils.getResourcePath(outputPath)+"export_multi_sheets_with_workbook.xls";
		System.out.println("path: " + path);
		TemplateGenerator g = DefaultExcelGeneratorFactory.createWorkbookGenerator("org/onetwo/common/excel/export_multi_sheets_with_workbook.xml", context);
//		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/export_multi_sheets_with_workbook.xml", context);
		g.generateIt();
		g.write(path);
	}
	
//	@Test
	public void testTemplateWithReflect(){
		System.out.println("===========================>>>>>testTemplateWithReflect ");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("data", list);
		String path = FileUtils.getResourcePath(outputPath)+"city_company_reflect.xls";
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/template_city_company.xml", context);
		g.generateIt();
		g.write(path);
	}
	
//	@Test
	public void testTemplateWithReflectOgnl(){
		System.out.println("===========================>>>>>testTemplateWithReflectOgnl ");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("data", list);
		String path = FileUtils.getResourcePath(outputPath)+"city_company_reflect_ognl.xls";
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/template_city_company_span_ognl.xml", context);
		g.generateIt();
		g.write(path);
	}
	
	public void testTemplateWithOgnl(){
//		UtilTimerStack.setActive(false);
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("data", list);
		String path = FileUtils.getResourcePath(outputPath)+"city_company_ognl.xls";
		TemplateGenerator g = DefaultExcelGeneratorFactory.createExcelGenerator("org/onetwo/common/excel/template_city_company_ognl.xml", context);
		g.generateIt();
		g.write(path);
	}

}
