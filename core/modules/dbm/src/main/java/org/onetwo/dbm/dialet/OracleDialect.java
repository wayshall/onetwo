package org.onetwo.dbm.dialet;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.event.DbmEventAction;
import org.onetwo.dbm.event.DbmdbEventListenerManager;
import org.onetwo.dbm.event.oracle.OracleBatchInsertEventListener;
import org.onetwo.dbm.event.oracle.OracleInsertEventListener;
import org.onetwo.dbm.mapping.DbmTypeMapping.OracleSqlTypeMapping;


public class OracleDialect extends AbstractDBDialect {

	public OracleDialect() {
		super(DBMeta.create(DataBase.Oracle));
		this.setSqlTypeMapping(new OracleSqlTypeMapping());
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
	public void addLimitedValue(DbmQueryValue params, String firstName, int firstResult, String maxName, int maxResults){
		params.setValue(maxName, getMaxResults(firstResult, maxResults));
		params.setValue(firstName, firstResult);
	}

	@Override
	public int getMaxResults(int first, int size){
		return first+size;
	}

	@Override
	protected void onDefaultDbEventListenerManager(DbmdbEventListenerManager listMg){
		super.onDefaultDbEventListenerManager(listMg);
		listMg.register(DbmEventAction.insert, LangUtils.newArrayList(new OracleInsertEventListener()));
		listMg.register(DbmEventAction.batchInsert, LangUtils.newArrayList(new OracleBatchInsertEventListener()));
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
