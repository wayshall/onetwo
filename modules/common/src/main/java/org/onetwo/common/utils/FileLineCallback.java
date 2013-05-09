package org.onetwo.common.utils;

/********
 * 按行读取回调
 * @author wayshall
 *
 */
public interface FileLineCallback {
	
	/*******
	 * 
	 * @param line
	 * @param lineIndex
	 * @return false 停止读取，一般返回true
	 */
	public boolean doWithLine(String line, int lineIndex);

}
