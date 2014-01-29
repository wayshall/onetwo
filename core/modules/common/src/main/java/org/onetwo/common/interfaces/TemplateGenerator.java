package org.onetwo.common.interfaces;

import java.io.OutputStream;

public interface TemplateGenerator {
	/****
	 * 生成excel
	 */
	public void generateIt();
	/***
	 * 生产到指定路径
	 * @param path
	 */
	public void generateTo(String path);

	public void write(OutputStream out);

	/***
	 * 写入到指定路径
	 * @param path
	 */
	public void write(String path);

}