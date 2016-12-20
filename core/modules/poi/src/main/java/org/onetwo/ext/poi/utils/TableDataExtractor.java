package org.onetwo.ext.poi.utils;


/****
 * sheet数据抽取接口
 * @author weishao
 *
 * @param <T>
 */
public interface TableDataExtractor<DATA, TABLE> {

	public DATA extractData(TABLE sheet);

}