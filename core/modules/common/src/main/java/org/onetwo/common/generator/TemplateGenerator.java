package org.onetwo.common.generator;

import java.io.File;
import java.io.OutputStream;

public interface TemplateGenerator {
	
	public String getFormat();
	
	/****
	 * 返回参考的datasource的数量
	 * @return
	 */
	public int generateIt();
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