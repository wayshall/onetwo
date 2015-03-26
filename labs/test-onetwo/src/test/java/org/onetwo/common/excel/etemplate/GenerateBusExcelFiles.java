package org.onetwo.common.excel.etemplate;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.plugins.batch.utils.BatchUtils;
import org.onetwo.plugins.batch.utils.SimpleFlatFileItemReader;
import org.springframework.batch.item.file.transform.FieldSet;

public class GenerateBusExcelFiles {
	
	
	/*public void test1(){
		ExcelTemplateGenerator g = new ExcelTemplateGenerator(template.getFile());
		ETemplateContext context = new ETemplateContext();
		context.putAll(model);

		g.generate(context, out);
	}*/
	
	@Test
	public void test(){
		String busid = "216";
//		BatchUtils.newFlatFileItemReader(path, linesToSkip, delimiter, title, mapper);
		String templateFilePaht = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\busdata\\template.xlsx";
		File templateFile = new File(templateFilePaht);

		String basePath = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\busdata\\"+busid+"-excel\\";
		
		File[] files = FileUtils.listFiles("D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\busdata\\"+busid, ".csv");
		System.out.println("files: " + files.length);
		Stream.of(files).forEach(f ->{
			System.out.println("f: " +f);
			SimpleFlatFileItemReader<BusData> reader = BatchUtils.newFlatFileItemReader(f.getPath(), 0, "\t", "", (FieldSet fs)->{
																			BusData data = new BusData();
																			data.setBusid(fs.getValues()[0]);
																			data.setStartDate(fs.getValues()[1]);
																			data.setEndDate(fs.getValues()[2]);
																			data.setAccountTotal(Types.convertValue(fs.getValues()[3], double.class));
																			data.setTransTotal(Types.convertValue(fs.getValues()[4], double.class));
																			return data;
																		});

			reader.fastOpen();
			List<BusData> datas = reader.readList();

			ExcelTemplateGenerator g = new ExcelTemplateGenerator(templateFile);
			ETemplateContext context = new ETemplateContext();
			context.put("dataList", datas);
			context.put("busid", datas.get(0).getBusid());

			g.generate(context, basePath+FileUtils.getFileNameWithoutExt(f.getName())+".xlsx");
			
		});
	}


	static class BusData {
		private String busid;
		private String startDate;
		private String endDate;
		private double accountTotal;
		private double transTotal;
		public String getBusid() {
			return busid;
		}
		public void setBusid(String busid) {
			this.busid = busid;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public double getAccountTotal() {
			return accountTotal;
		}
		public void setAccountTotal(double accountTotal) {
			this.accountTotal = accountTotal;
		}
		public double getTransTotal() {
			return transTotal;
		}
		public void setTransTotal(double transTotal) {
			this.transTotal = transTotal;
		}
		
	}
}
