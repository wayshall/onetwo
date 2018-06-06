package org.onetwo.boot.core.web.mvc.log;


import java.util.Map;

import org.onetwo.common.log.DataChangedContext.DataOperateType;
import org.onetwo.common.log.DataChangedContext.EntityState;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

public class FileAccessLogger implements InitializingBean {

	public static final String LOGGER_KEY = "accessLogger";
	private Logger accessLogger;
	private String loggerName;
	private String seprator = " ";
	private boolean logChangedDatas;
	private boolean logControllerDatas;
//	private boolean debug;
	

	public FileAccessLogger() {
		this(LOGGER_KEY);
	}
	public FileAccessLogger(String loggerName) {
		super();
		this.loggerName = loggerName;
	}

	public void setLogControllerDatas(boolean logControllerDatas) {
		this.logControllerDatas = logControllerDatas;
	}
	public void setLogChangedDatas(boolean logChangedDatas) {
		this.logChangedDatas = logChangedDatas;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initLogger();
	}
	
//	@Override
	public void initLogger() {
		accessLogger = createAccessLoggerLogger();// JFishLoggerFactory.getLogger(LOGGER_KEY);
		Assert.notNull(accessLogger, "accessLogger not null");
	}

	protected Logger createAccessLoggerLogger(){
		if(StringUtils.isBlank(loggerName)){
			loggerName = LOGGER_KEY;
		}
		return JFishLoggerFactory.getLogger(loggerName);
	}
	
	@EventListener
	public void onLogEvent(OperatorLogEvent event){
		logOperation(event.getOperatorLog());
	}
	
//	@Override
	public void logOperation(OperatorLogInfo data){
		if(accessLogger.isInfoEnabled()){
			doLogOperation(data);
		}
	}
	
	protected void doLogOperation(OperatorLogInfo data){
//		ToStringBuilder buf = new ToStringBuilder(data, ToStringStyle.MULTI_LINE_STYLE);
		StringBuilder buf = new StringBuilder(300);
		buf.append("{").append(seprator)
			.append("url:").append(data.getUrl()).append(",").append(seprator)
			.append("executedTimeInMillis:").append(data.getExecutedTimeInMillis()).append(",").append(seprator)
			.append("operatorId:").append(data.getOperatorId()).append(",").append(seprator)
			.append("operatorName:").append(data.getOperatorName()).append(",").append(seprator)
			.append("success:").append(data.isSuccess()).append(",").append(seprator)
			.append("message:").append(data.getMessage()).append(",").append(seprator)
			.append("remoteAddr:").append(data.getRemoteAddr()).append(",").append(seprator);
		
		if(logControllerDatas){
			buf.append("params:").append(buildParametersString(data.getParameters())).append(",").append(seprator)
				.append("webHandler:").append(data.getWebHandler()).append(",").append(seprator);
		}
			
		if(logChangedDatas){
			StringBuilder changedBuf = new StringBuilder(100);
			StringBuilder inserts = new StringBuilder();//最少14
			StringBuilder deletes = new StringBuilder();
			StringBuilder updates = new StringBuilder();
			
			if(data.getDatas()!=null && data.getDatas().getChangedList()!=null){
				for(EntityState state : data.getDatas().getChangedList()){
					String str = buildString(state);
					switch (state.getOperationType()) {
					case INSERT:
						inserts.append(str).append(", ");
						break;
					case UPDATE:
						updates.append(str).append(", ");
						break;
					case DELETE:
						deletes.append(str).append(", ");
						break;
					default:
						break;
					}
				}
			}
			changedBuf.append("changedDatas:[").append(seprator)
				.append("{inserts:[").append(inserts).append("]},").append(seprator)
				.append("{updates:[").append(updates).append("]},").append(seprator)
				.append("{deletes:[").append(deletes).append("]}").append(seprator)
				.append("]}");
			buf.append(changedBuf);
		}
		
		if(accessLogger.isInfoEnabled()){
			accessLogger.info(buf.toString());
		}
	}
	
	private String buildParametersString(Map<String, String[]> parameters){
		if(LangUtils.isEmpty(parameters))
			return "";
		return LangUtils.toString(parameters);
	}
	
	protected boolean logThisStateType(Object type){
		return true;
	}

	public String buildString(EntityState entity){
		StringBuilder buf = new StringBuilder();
		buf.append("{")
			.append(entity.getEntity()).append(":").append(entity.getId())
			.append(", fields:[");

		buf.append("{");
		int i = 0;
		for(String prop : entity.getPropertyNames()){
			if(i!=0)
				buf.append(", ");
			buf.append(prop).append(": ");

			Object type = LangUtils.safeGetValue(entity.getTypes(), i);
//			if(SingleColumnType.class.isInstance(type) || CustomType.class.isInstance(type) || EnumType.class.isInstance(type) || UserVersionType.class.isInstance(type)){
			if(logThisStateType(type)){
				Object current = LangUtils.safeGetValue(entity.getCurrentState(), i);
				
				buf.append("[");
				if(entity.getOperationType()==DataOperateType.UPDATE){
					Object old = LangUtils.safeGetValue(entity.getPreviousState(), i);
					buf.append(old).append(", ").append(current);
				}else{
					buf.append(current);
				}
				buf.append("]");
			}
			i++;
		}
		
		buf.append("}").append("]");
		return buf.toString();
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public void setSeprator(String seprator) {
		this.seprator = seprator;
	}
	
}
