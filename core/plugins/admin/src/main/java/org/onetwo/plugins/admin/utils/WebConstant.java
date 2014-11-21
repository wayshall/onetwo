package org.onetwo.plugins.admin.utils;

public class WebConstant {

	public static final String SEQ_TABLE_NAME = "SEQ_TABLES";
	public static final String METHOD_CACHE_ETERNAL = "METHOD_CACHE_ETERNAL";

	public static interface ValidGroup {
		public static interface ValidWhenNew {}
		public static interface ValidWhenEdit {}
		
		public static interface Password {}
	}
	
	
	private WebConstant(){
	}
}
