package org.onetwo.common.jfishdbm.dialet;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.jfishdbm.event.JFishEventAction;
import org.onetwo.common.jfishdbm.event.JFishdbEventListenerManager;
import org.onetwo.common.jfishdbm.event.oracle.JFishOracleBatchInsertEventListener;
import org.onetwo.common.jfishdbm.event.oracle.JFishOracleInsertEventListener;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class OracleDialect extends AbstractDBDialect {

	public OracleDialect() {
		super(DBMeta.create(DataBase.Oracle));
	}

	public void registerIdStrategy(){
		this.getIdStrategy().add(StrategyType.SEQ);
	}
	
	public String getLimitString(String sql, String firstName, String maxResultName) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}

		boolean hasOffset = true;
		if(StringUtils.isBlank(firstName)){
			firstName = "?";
		}else{
			firstName = ":"+firstName;
		}
		if(StringUtils.isBlank(maxResultName)){
			maxResultName = "?";
		}else{
			maxResultName = ":"+maxResultName;
		}
		
		StringBuffer pagingSelect = new StringBuffer();
		if (hasOffset) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		
		boolean isOrderBy = sql.indexOf(" order by ")!=-1 && sql.indexOf("group by")==-1;
		if(isOrderBy)
			pagingSelect.append(", rownum");
		
		if (hasOffset) {
			pagingSelect.append(" ) row_ where rownum <= ").append(maxResultName).append(") where rownum_ > ").append(firstName);
		}
		else {
			pagingSelect.append(" ) where rownum <= ").append(maxResultName);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}

		return pagingSelect.toString();
	}

	@Override
	public void addLimitedValue(JFishQueryValue params, String firstName, int firstResult, String maxName, int maxResults){
		params.setValue(maxName, getMaxResults(firstResult, maxResults));
		params.setValue(firstName, firstResult);
	}

	@Override
	public int getMaxResults(int first, int size){
		return first+size;
	}

	@Override
	protected void onDefaultDbEventListenerManager(JFishdbEventListenerManager listMg){
		listMg.register(JFishEventAction.insert, LangUtils.newArrayList(new JFishOracleInsertEventListener()));
		listMg.register(JFishEventAction.batchInsert, LangUtils.newArrayList(new JFishOracleBatchInsertEventListener()));
	}
	/*protected DbEventListenerManager createDefaultDbEventListenerManager() {
		JFishdbEventListenerManager listenerManager = new JFishdbEventListenerManager() {

			protected InsertEventListener getDefaultInsertEventListener() {
				return new JFishOracleInsertEventListener();
			}

			protected InsertEventListener getDefaultBatchInsertEventListener() {
//				JFishOracleInsertEventListener ie = new JFishOracleInsertEventListener();
				JFishOracleInsertEventListener ie = new JFishOracleBatchInsertEventListener();
				return ie;
			}

		};
		return listenerManager;
	}*/
	
	

}
