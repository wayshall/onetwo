package com.qyscard.o2o.utils;


import org.hibernate.type.CustomType;
import org.hibernate.type.EnumType;
import org.hibernate.type.SingleColumnType;
import org.hibernate.usertype.UserVersionType;
import org.onetwo.common.log.DataChangedContext.DataOperateType;
import org.onetwo.common.log.DataChangedContext.EntityState;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.log.AccessLogger;
import org.onetwo.common.spring.web.mvc.log.OperatorLogInfo;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class IccardAccessLogger implements AccessLogger {

	public static final String LOGGER_KEY = "iccardAccessLogger";
	private static final Logger accessLogger = MyLoggerFactory.getLogger(LOGGER_KEY);
	
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
		
		StringBuilder buf = new StringBuilder();
		buf.append("{operatorId:").append(data.getOperatorId()).append(", ")
			.append("operatorName:").append(data.getOperatorName()).append(", ")
			.append("success:").append(data.isSuccess()).append(", ")
			.append("message:").append(data.getMessage()).append(", ")
			.append("url:").append(data.getUrl()).append(", ")
			.append("changedDatas:[")
			.append("{inserts:[").append(inserts).append("]},")
			.append("{updates:[").append(updates).append("]},")
			.append("{deletes:[").append(deletes).append("]}")
			.append("]}");
		
		accessLogger.info(buf.toString());
		
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
			if(SingleColumnType.class.isInstance(type) || CustomType.class.isInstance(type) || EnumType.class.isInstance(type) || UserVersionType.class.isInstance(type)){
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
