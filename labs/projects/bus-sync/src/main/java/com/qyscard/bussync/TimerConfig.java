package com.qyscard.bussync;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class TimerConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(TimerConfig.class);

	private static final TimerConfig instance = new TimerConfig();
	
	public static TimerConfig getInstance() {
		return instance;
	}

	protected TimerConfig() {
		super("siteConfig.properties");
	}
	
	public String getDataSyncTaskCronExpression(){
		return getAndThrowIfEmpty("dataSyncTask.cronExpression");
	}
	
	public String  getIp(){
		return getProperty("tac.ip");
	}
	
	public String  getPort(){
		return getProperty("tac.port");
	}
	
	public int  getEveryTime(){
		String num = getProperty("every.time");
		if(num==null||"".equals(num)){
			return 0;
		}else{
			return  Integer.parseInt(num); 
		}
		
		
	}
}
