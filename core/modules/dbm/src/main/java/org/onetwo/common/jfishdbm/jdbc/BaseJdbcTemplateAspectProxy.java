package org.onetwo.common.jfishdbm.jdbc;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class BaseJdbcTemplateAspectProxy {
	
	protected static class Context {
		private int jdbcCountInThread;
		private final long startTime;
		private long finishTime;
		private Object returnValue;
		private long costTime;

		public Context(long startTime) {
			super();
			this.startTime = startTime;
		}

		public long getFinishTime() {
			return finishTime;
		}

		public void setFinishTime(long finishTime) {
			this.finishTime = finishTime;
			this.costTime = finishTime-startTime;
		}

		public Object getReturnValue() {
			return returnValue;
		}

		public void setReturnValue(Object returnValue) {
			this.returnValue = returnValue;
		}

		public long getStartTime() {
			return startTime;
		}
		public long getCostTime(){
			return costTime;
		}
		public int getJdbcCountInThread() {
			return jdbcCountInThread;
		}

		public void setJdbcCountInThread(int jdbcCountInThread) {
			this.jdbcCountInThread = jdbcCountInThread;
		}

		public String toString(){
			StringBuilder str = new StringBuilder("jdbcCountInThread: ");
			str.append(jdbcCountInThread).append("\n")
			.append("cost time(millisecond): ").append(costTime).append("\n");
			if(returnValue!=null){
				str.append("return value: ");
				if(LangUtils.isBaseTypeObject(returnValue)){
					str.append(returnValue);
				}else if(returnValue.getClass().isArray()){
					Class<?> ctype = returnValue.getClass().getComponentType();
					if(ctype==int.class){
						str.append(LangUtils.sum((int[])returnValue));
					}
				}
				str.append("\n");
			}
			return str.toString();
		}
	}
	
	protected final Logger logger = MyLoggerFactory.getLogger(BaseJdbcTemplateAspectProxy.class);

	protected void afterProceed(Context context, ProceedingJoinPoint pjp){
		this.printLog(context, pjp.getSignature().toString(), pjp.getArgs());
	}

	protected void printLog(Context context, String name, Object[] args){
		try {

			if(logger.isInfoEnabled()){
				StringBuilder logMsg = new StringBuilder(name).append("\n");
				String sql = null;
				Object mArgs = null;
				
				for(Object arg : args){
					if(arg==null)
						continue;
					if(sql==null && String.class.isInstance(arg)){
						sql = arg.toString();
					}else if(mArgs==null && LangUtils.isMultiple(arg)){
						mArgs = arg;
					}else if(mArgs==null && Map.class.isInstance(arg)){
						mArgs = (Map)arg;
					}else if(MapSqlParameterSource.class.isInstance(arg)){
						MapSqlParameterSource c = (MapSqlParameterSource) arg;
						mArgs = c.getValues();
					}else if(SimpleArgsPreparedStatementCreator.class.isInstance(arg)){
						SimpleArgsPreparedStatementCreator c = (SimpleArgsPreparedStatementCreator) arg;
						sql = c.getSql();
						mArgs = c.getArgs();
					}else if(SqlProvider.class.isInstance(arg)){
						sql = ((SqlProvider) arg).getSql();
						if(PreparedStatementSetter.class.isInstance(arg)){
							mArgs = ReflectUtils.getFieldValue(arg, "parameters");
							if(mArgs==null)
								mArgs = ReflectUtils.getFieldValue(arg, "args");
						}
					}
				}
				logMsg.append("sql: ").append(sql).append("\nsql args: ").append(LangUtils.toString(mArgs)).append("\n");
				logMsg.append(context).append("\n");
				logger.info(logMsg.toString());
			}
			
		} catch (Throwable e) {
			logger.error("log jdbc error : " + e.getMessage(), e);
		}
	}
	
	protected void printSql(String name, SimpleArgsPreparedStatementCreator spsc){
		printSql(name, spsc.getSql(), spsc.getArgs());
	}
	
	protected void printSql(String name, String sql, Object args){
		String pname = StringUtils.trimToEmpty(name);
		logger.info(pname+" sql: " + sql);
		logger.info(pname+" args: " + LangUtils.toString(args));
	}

}
