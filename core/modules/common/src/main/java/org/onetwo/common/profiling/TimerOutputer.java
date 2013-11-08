package org.onetwo.common.profiling;


public class TimerOutputer implements TimeLogger {
	
	@Override
	public void log(String msg){
		System.out.println("==================>>>"+msg);
	}

}
