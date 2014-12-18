package org.onetwo.common.interfaces;

import java.io.File;
import java.io.OutputStream;

public interface TemplateGenerator {
	
	public String getFormat();
	/****
	 * 生成excel
	 */
	public void generateIt();
	/***
	 * 生产到指定路径
	 * @param path
	 */
	public File generateTo(String path);

	public void write(OutputStream out);

	/***
	 * 写入到指定路径
	 * @param path
	 */
	public File write(String path);

}