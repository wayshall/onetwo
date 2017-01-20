package org.onetwo.dbm.jdbc;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.onetwo.common.expr.HolderCharsScanner;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MathUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.google.common.collect.Lists;

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
						str.append(MathUtils.sum((int[])returnValue));
					}
				}
				str.append("\n");
			}
			return str.toString();
		}
	}
	
	protected final Logger logger = JFishLoggerFactory.getLogger(BaseJdbcTemplateAspectProxy.class);
	protected final HolderCharsScanner holder = HolderCharsScanner.holder("?");
//	private static boolean printRelacedSql = true;
	
	
	/*public static void setPrintRelacedSql(boolean _printRelacedSql) {
		printRelacedSql = _printRelacedSql;
	}*/

	protected void afterProceed(Context context, ProceedingJoinPoint pjp){
		this.printLog(context, pjp.getSignature().toString(), pjp.getArgs());

		StringBuilder logMsg = new StringBuilder(pjp.getSignature().toString()).append("\n");
		logMsg.append(context).append("\n");
		logger.info(logMsg.toString());
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
						mArgs = (Map<?, ?>)arg;
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
				
				List<?> argList = convertAsList(mArgs);
				String parseArgSql = holder.parse(sql, index->{
					Object val = argList.get(index);
					val = DbmUtils.getActualValue(val);
					if(val==null)
						return "NULL";
					return LangUtils.isNumberObject(val)?val.toString():"'"+val.toString()+"'";
				});
				logMsg.append("replaced arg sql:").append(parseArgSql).append("\n");
				
				logMsg.append(context).append("\n");
				logger.info(logMsg.toString());
			}
			
		} catch (Throwable e) {
			logger.error("log jdbc error : " + e.getMessage());
		}
	}
	
	protected List<?> convertAsList(Object args){
		if(Iterable.class.isInstance(args)){
			return Lists.newArrayList((Iterable<?>)args);
		}else if(Map.class.isInstance(args)){
			Map<?, ?> map = (Map<?, ?>) args;
			return Lists.newArrayList(map.values());
		}else{
			return CUtils.tolist(args, false);
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
