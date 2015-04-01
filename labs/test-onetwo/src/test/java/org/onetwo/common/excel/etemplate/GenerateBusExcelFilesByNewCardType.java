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

public class GenerateBusExcelFilesByNewCardType {
	
	
	/*public void test1(){
		ExcelTemplateGenerator g = new ExcelTemplateGenerator(template.getFile());
		ETemplateContext context = new ETemplateContext();
		context.putAll(model);

		g.generate(context, out);
	}*/
	
	@Test
	public void test(){
		String busid = "all";
//		BatchUtils.newFlatFileItemReader(path, linesToSkip, delimiter, title, mapper);
		String templateFilePaht = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype_new\\template.xlsx";
		File templateFile = new File(templateFilePaht);

		String basePath = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype_new\\"+busid+"-excel\\";
		
		File[] files = FileUtils.listFiles("D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype_new\\"+busid, ".csv");
		System.out.println("files: " + files.length);
		Stream.of(files).forEach(f ->{
			System.out.println("f: " +f);
			SimpleFlatFileItemReader<BusData> reader = BatchUtils.newFlatFileItemReader(f.getPath(), 0, "\t", "", (FieldSet fs)->{
																			int index = 0;
																			BusData data = new BusData();
																			data.setBusid(fs.getValues()[index++]);
																			data.setStartDate(fs.getValues()[index++]);
																			data.setEndDate(fs.getValues()[index++]);
																			
																			data.setTfByAccount(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setOldcardTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setYinlianTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setLingnantongTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setBcardTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			
																			data.setTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setOldAndYinlianByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setBcardSubsidyByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			
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

		private double tfByAccount;

		private double oldcardTfByOpdt;
		private double bcardTfByOpdt;
		private double yinlianTfByOpdt;
		private double lingnantongTfByOpdt;
		
		private double tfByOpdt;
		private double oldAndYinlianByOpdt;
		private double bcardSubsidyByOpdt;
		
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
		public double getOldcardTfByOpdt() {
			return oldcardTfByOpdt;
		}
		public void setOldcardTfByOpdt(double oldcardTfByOpdt) {
			this.oldcardTfByOpdt = oldcardTfByOpdt;
		}
		public double getBcardTfByOpdt() {
			return bcardTfByOpdt;
		}
		public void setBcardTfByOpdt(double bcardTfByOpdt) {
			this.bcardTfByOpdt = bcardTfByOpdt;
		}
		public double getYinlianTfByOpdt() {
			return yinlianTfByOpdt;
		}
		public void setYinlianTfByOpdt(double yinlianTfByOpdt) {
			this.yinlianTfByOpdt = yinlianTfByOpdt;
		}
		public double getLingnantongTfByOpdt() {
			return lingnantongTfByOpdt;
		}
		public void setLingnantongTfByOpdt(double lingnantongTfByOpdt) {
			this.lingnantongTfByOpdt = lingnantongTfByOpdt;
		}
		public double getTfByAccount() {
			return tfByAccount;
		}
		public void setTfByAccount(double tfByAccount) {
			this.tfByAccount = tfByAccount;
		}
		public double getTfByOpdt() {
			return tfByOpdt;
		}
		public void setTfByOpdt(double tfByOpdt) {
			this.tfByOpdt = tfByOpdt;
		}
		public double getOldAndYinlianByOpdt() {
			return oldAndYinlianByOpdt;
		}
		public void setOldAndYinlianByOpdt(double oldAndYinlianByOpdt) {
			this.oldAndYinlianByOpdt = oldAndYinlianByOpdt;
		}
		public double getBcardSubsidyByOpdt() {
			return bcardSubsidyByOpdt;
		}
		public void setBcardSubsidyByOpdt(double bcardSubsidyByOpdt) {
			this.bcardSubsidyByOpdt = bcardSubsidyByOpdt;
		}
		
	}
}
