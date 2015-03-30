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

public class GenerateBusExcelFilesByCardType {
	
	
	/*public void test1(){
		ExcelTemplateGenerator g = new ExcelTemplateGenerator(template.getFile());
		ETemplateContext context = new ETemplateContext();
		context.putAll(model);

		g.generate(context, out);
	}*/
	
	@Test
	public void test(){
		String busid = "213";
//		BatchUtils.newFlatFileItemReader(path, linesToSkip, delimiter, title, mapper);
		String templateFilePaht = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype\\template.xlsx";
		File templateFile = new File(templateFilePaht);

		String basePath = "D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype\\"+busid+"-excel\\";
		
		File[] files = FileUtils.listFiles("D:\\mydev\\workdoc\\公交报表\\有问题报表-03-23\\bycardtype\\"+busid, ".csv");
		System.out.println("files: " + files.length);
		Stream.of(files).forEach(f ->{
			System.out.println("f: " +f);
			SimpleFlatFileItemReader<BusData> reader = BatchUtils.newFlatFileItemReader(f.getPath(), 0, "\t", "", (FieldSet fs)->{
																			int index = 0;
																			BusData data = new BusData();
																			data.setBusid(fs.getValues()[index++]);
																			data.setStartDate(fs.getValues()[index++]);
																			data.setEndDate(fs.getValues()[index++]);
																			
																			data.setOldcardTfByAccount(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setOldcardTimesByAccount(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setYinlianTfByAccount(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setYinlianTimesByAccount(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setLingnantongTfByAccount(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setLingnantongTimesByAccount(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setBcardTfByAccount(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setBcardTimesByAccount(Types.convertValue(fs.getValues()[index++], int.class, 0));

																			
																			data.setOldcardTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setOldcardTimesByOpdt(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setYinlianTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setYinlianTimesByOpdt(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setLingnantongTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setLingnantongTimesByOpdt(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			data.setBcardTfByOpdt(Types.convertValue(fs.getValues()[index++], double.class, 0D));
																			data.setBcardTimesByOpdt(Types.convertValue(fs.getValues()[index++], int.class, 0));
																			
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

		private double oldcardTfByAccount;
		private int oldcardTimesByAccount;
		private double bcardTfByAccount;
		private int bcardTimesByAccount;
		private double yinlianTfByAccount;
		private int yinlianTimesByAccount;
		private double lingnantongTfByAccount;
		private int lingnantongTimesByAccount;

		private double oldcardTfByOpdt;
		private int oldcardTimesByOpdt;
		private double bcardTfByOpdt;
		private int bcardTimesByOpdt;
		private double yinlianTfByOpdt;
		private int yinlianTimesByOpdt;
		private double lingnantongTfByOpdt;
		private int lingnantongTimesByOpdt;
		
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
		public double getOldcardTfByAccount() {
			return oldcardTfByAccount;
		}
		public void setOldcardTfByAccount(double oldcardTfByAccount) {
			this.oldcardTfByAccount = oldcardTfByAccount;
		}
		public int getOldcardTimesByAccount() {
			return oldcardTimesByAccount;
		}
		public void setOldcardTimesByAccount(int oldcardTimesByAccount) {
			this.oldcardTimesByAccount = oldcardTimesByAccount;
		}
		public double getBcardTfByAccount() {
			return bcardTfByAccount;
		}
		public void setBcardTfByAccount(double bcardTfByAccount) {
			this.bcardTfByAccount = bcardTfByAccount;
		}
		public int getBcardTimesByAccount() {
			return bcardTimesByAccount;
		}
		public void setBcardTimesByAccount(int bcardTimesByAccount) {
			this.bcardTimesByAccount = bcardTimesByAccount;
		}
		public double getYinlianTfByAccount() {
			return yinlianTfByAccount;
		}
		public void setYinlianTfByAccount(double yinlianTfByAccount) {
			this.yinlianTfByAccount = yinlianTfByAccount;
		}
		public int getYinlianTimesByAccount() {
			return yinlianTimesByAccount;
		}
		public void setYinlianTimesByAccount(int yinlianTimesByAccount) {
			this.yinlianTimesByAccount = yinlianTimesByAccount;
		}
		public double getLingnantongTfByAccount() {
			return lingnantongTfByAccount;
		}
		public void setLingnantongTfByAccount(double lingnantongTfByAccount) {
			this.lingnantongTfByAccount = lingnantongTfByAccount;
		}
		public int getLingnantongTimesByAccount() {
			return lingnantongTimesByAccount;
		}
		public void setLingnantongTimesByAccount(int lingnantongTimesByAccount) {
			this.lingnantongTimesByAccount = lingnantongTimesByAccount;
		}
		public double getOldcardTfByOpdt() {
			return oldcardTfByOpdt;
		}
		public void setOldcardTfByOpdt(double oldcardTfByOpdt) {
			this.oldcardTfByOpdt = oldcardTfByOpdt;
		}
		public int getOldcardTimesByOpdt() {
			return oldcardTimesByOpdt;
		}
		public void setOldcardTimesByOpdt(int oldcardTimesByOpdt) {
			this.oldcardTimesByOpdt = oldcardTimesByOpdt;
		}
		public double getBcardTfByOpdt() {
			return bcardTfByOpdt;
		}
		public void setBcardTfByOpdt(double bcardTfByOpdt) {
			this.bcardTfByOpdt = bcardTfByOpdt;
		}
		public int getBcardTimesByOpdt() {
			return bcardTimesByOpdt;
		}
		public void setBcardTimesByOpdt(int bcardTimesByOpdt) {
			this.bcardTimesByOpdt = bcardTimesByOpdt;
		}
		public double getYinlianTfByOpdt() {
			return yinlianTfByOpdt;
		}
		public void setYinlianTfByOpdt(double yinlianTfByOpdt) {
			this.yinlianTfByOpdt = yinlianTfByOpdt;
		}
		public int getYinlianTimesByOpdt() {
			return yinlianTimesByOpdt;
		}
		public void setYinlianTimesByOpdt(int yinlianTimesByOpdt) {
			this.yinlianTimesByOpdt = yinlianTimesByOpdt;
		}
		public double getLingnantongTfByOpdt() {
			return lingnantongTfByOpdt;
		}
		public void setLingnantongTfByOpdt(double lingnantongTfByOpdt) {
			this.lingnantongTfByOpdt = lingnantongTfByOpdt;
		}
		public int getLingnantongTimesByOpdt() {
			return lingnantongTimesByOpdt;
		}
		public void setLingnantongTimesByOpdt(int lingnantongTimesByOpdt) {
			this.lingnantongTimesByOpdt = lingnantongTimesByOpdt;
		}
	}
}
