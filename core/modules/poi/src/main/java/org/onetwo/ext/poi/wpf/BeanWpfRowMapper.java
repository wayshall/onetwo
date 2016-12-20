package org.onetwo.ext.poi.wpf;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.TableHeaderMapper;
import org.onetwo.ext.poi.utils.TableHeaderMapper.MapperBean;


public class BeanWpfRowMapper<T> extends AbstractWpfRowMapper<T> {

	private TableHeaderMapper tableHeaderMapper;
	private Map<String, String> headerMapper;
	private Class<T> beanClass;
	
	public BeanWpfRowMapper(Map<String, String> headerMapper, Class<T> clazz) {
		super();
		this.headerMapper = headerMapper;
		this.beanClass = clazz;
	}
	

	@Override
	public List<String> mapTitleRow(XWPFTable table) {
		List<String> titles = super.mapTitleRow(table);
		if(tableHeaderMapper==null){
			TableHeaderMapper t = new TableHeaderMapper(titles.toArray(new String[0]));
			t.mapAll(headerMapper);
			this.tableHeaderMapper = t;
		}
		return titles;
	}

	@Override
	public int getDataRowStartIndex() {
		return 1;
	}

	@Override
	public T mapDataRow(XWPFTable sheet, List<String> names, int rowIndex) {
		XWPFTableRow row = sheet.getRow(rowIndex);
		T bean = ExcelUtils.newInstance(beanClass);
		MapperBean<T> mapperBean = tableHeaderMapper.createMapperBean(bean);
		IntStream.range(0, row.getTableCells().size()).forEach(index->{
			XWPFTableCell cell = row.getTableCells().get(index);
			mapperBean.setValue(index, StringUtils.trim(cell.getText()));
		});
		return mapperBean.getBean();
	}
	
}
