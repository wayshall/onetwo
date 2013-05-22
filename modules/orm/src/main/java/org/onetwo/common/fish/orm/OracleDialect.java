package org.onetwo.common.fish.orm;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.utils.StringUtils;


public class OracleDialect extends AbstractDBDialect {

	public OracleDialect(DataBaseConfig dataBaseConfig) {
		super(dataBaseConfig);
	}

	public void registerIdStrategy(){
		this.getIdStrategy().add(StrategyType.seq);
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

}
