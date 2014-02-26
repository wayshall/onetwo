package org.onetwo.common.web.view.jsp.datagrid;

import org.onetwo.common.web.view.jsp.datagrid.DataRowTagBean.CurrentRowData;

/****
 * dataFiled的value被解释后的监听器，纯粹预留。。。。。。。
 * @author weishao
 *
 */
public interface DataFieldValueListener {

	void afterTranslateValue(CurrentRowData rowData, DataFieldTag fieldTag, Object dataFieldValue);
}
