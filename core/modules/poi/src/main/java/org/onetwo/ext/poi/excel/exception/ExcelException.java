package org.onetwo.ext.poi.excel.exception;



@SuppressWarnings("serial")
public class ExcelException extends RuntimeException {

	public ExcelException(String msg) {
		super(msg);
	}

	public ExcelException(Throwable cause) {
		super(cause);
	}


	public ExcelException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
