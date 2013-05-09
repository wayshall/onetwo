package org.onetwo.common.fish.utils;

final public class JFishHolder {
	
	private static final ThreadLocal<JdbcContext> jdbcContextHolder = new JFishThreadLocal<JdbcContext>("Jdbc Context");
	
	
	public static JdbcContext getJdbcContex(){
		JdbcContext context = jdbcContextHolder.get();
		if(context==null){
			context = new JdbcContext();
			jdbcContextHolder.set(context);
		}
		return context;
	}
	
	public static void setJdbcContext(JdbcContext context){
		jdbcContextHolder.set(null);
	}
	
	private JFishHolder(){}

}
