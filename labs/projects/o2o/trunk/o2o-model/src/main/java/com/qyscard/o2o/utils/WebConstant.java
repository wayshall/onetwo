package com.qyscard.o2o.utils;


final public class WebConstant {


	public static interface ValidGroup {
		public static interface ValidWhenNew {}
		public static interface ValidWhenEdit {}
		
		public static interface Password {}
	}
	
	public static interface TMNames{
		String BUS = "bus";
	}
	
	/****
	 * psam卡号长度
	 */
	public static final int PSAM_NO_LENGTH = 20;
	
	public static final String SEQ_TABLE_NAME = "SEQ_TABLES";
	
	private WebConstant(){
	}
	
}
