package org.onetwo.common.fish.utils;

final public class JFishHolder {

	private static final ThreadLocal<JdbcContext> jdbcContextHolder = new JFishThreadLocal<JdbcContext>("Jdbc Context");
	private static final ThreadLocal<ProfilerContext> profileContextHolder = new JFishThreadLocal<ProfilerContext>("Profile Context");

	
	public static JdbcContext getJdbcContex(){
		JdbcContext context = jdbcContextHolder.get();
		if(context==null){
			context = new JdbcContext();
			jdbcContextHolder.set(context);
		}
		return context;
	}
	
	public static void setJdbcContext(JdbcContext context){
		jdbcContextHolder.set(context);
	}
	
	public static ProfilerContext getProfilerContext(){
		ProfilerContext context = profileContextHolder.get();
		if(context==null){
			context = new ProfilerContext();
			profileContextHolder.set(context);
		}
		return context;
	}
	
	public static void setProfilerContext(ProfilerContext context){
		profileContextHolder.set(null);
	}
	
	public static void reset(){
		jdbcContextHolder.remove();
		profileContextHolder.remove();
	}
	
	
	private JFishHolder(){}

}
