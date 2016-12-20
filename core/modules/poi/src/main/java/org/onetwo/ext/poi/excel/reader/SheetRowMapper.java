package org.onetwo.ext.poi.excel.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.utils.TableRowMapper;

public interface SheetRowMapper<DATA> extends TableRowMapper<DATA, Sheet> {

}
