package org.onetwo.ext.poi.excel.interfaces;

import java.io.File;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;

public interface TemplateGenerator {
	
	public String getFormat();
	
	/****
	 * 返回参考的datasource的数量
	 * @return
	 */
	public Workbook generateIt();

	public void write(OutputStream out);

	/***
	 * 写入到指定路径
	 * @param path
	 */
	public File write(String path);

}