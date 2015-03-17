package org.onetwo.common.spring.web.mvc.log;


import java.util.Map;

import org.onetwo.common.log.DataChangedContext.DataOperateType;
import org.onetwo.common.log.DataChangedContext.EntityState;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class DefaultAccessLogger implements AccessLogger {

	public static final String LOGGER_KEY = "iccardAccessLogger";
	private final Logger accessLogger = JFishLoggerFactory.getLogger(LOGGER_KEY);
	
	@Override
	public void logOperation(OperatorLogInfo data){
		StringBuilder inserts = new StringBuilder();
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
		
//		ToStringBuilder buf = new ToStringBuilder(data, ToStringStyle.MULTI_LINE_STYLE);
		StringBuilder buf = new StringBuilder();
		buf.append("{")
			.append("executedTimeInMillis:"+data.getExecutedTimeInMillis()+", ")
			.append("operatorId:"+data.getOperatorId()+", ")
			.append("operatorName:"+data.getOperatorName()+", ")
			.append("success:"+data.isSuccess()+", ")
			.append("message:"+data.getMessage()+", ")
			.append("url:"+data.getUrl()+", ")
			.append("remoteAddr:"+data.getRemoteAddr()+", ")
			.append("params:"+buildParametersString(data.getParameters())+", ")
			.append("changedDatas:[")
			.append("{inserts:["+inserts+"]},")
			.append("{updates:["+updates+"]},")
			.append("{deletes:["+deletes+"]}")
			.append("]}");
		
		accessLogger.info(buf.toString());
		
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
}
