package org.onetwo.ext.poi.wpf;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.onetwo.ext.poi.utils.TableRowMapper;

abstract public class AbstractWpfRowMapper<T> implements TableRowMapper<T, XWPFTable> {

	@Override
	public int getNumberOfRows(XWPFTable table) {
		return table.getRows().size();
	}

	@Override
	public List<String> mapTitleRow(XWPFTable table) {
		XWPFTableRow row = table.getRow(0);
		return row.getTableCells().stream().map(cell->{
			return StringUtils.trim(cell.getText());
		}).collect(Collectors.toList());
	}

	@Override
	public int getDataRowStartIndex() {
		return 1;
	}

	@Override
	public T mapDataRow(XWPFTable table, List<String> names, int rowIndex) {
		XWPFTableRow row = table.getRow(rowIndex);
		// TODO Auto-generated method stub
		return null;
	}
	
}
