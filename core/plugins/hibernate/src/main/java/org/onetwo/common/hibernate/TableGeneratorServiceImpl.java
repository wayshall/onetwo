package org.onetwo.common.hibernate;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.Expression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TableGeneratorServiceImpl implements InitializingBean, TableGeneratorService {
	/*private final static String SEQ_SELECT_SQL = "select tbl.GEN_VALUE from SEQ_TABLES tbl with (updlock, rowlock ) where tbl.GEN_NAME= :seqName";
	private final static String SEQ_UPDATE_SQL = " update SEQ_TABLES set GEN_VALUE=:newSeqValue where GEN_VALUE=:seqValue and GEN_NAME=:seqName";
	private final static String SEQ_INSERT_SQL = " insert into SEQ_TABLES (GEN_NAME, GEN_VALUE) values (:seqName, :seqValue)";*/
	
	private Expression exp = Expression.DOLOR;
	private String tableName = "SEQ_TABLES";
	private String seqNameField = "GEN_NAME";
	private String seqValueField = "GEN_VALUE";
	
	private String selectSql;
	private String updateSql;
	private String insertSql;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		selectSql = exp.parse("select tbl.${seqValueField} from ${tableName} tbl with (updlock, rowlock ) where tbl.${seqNameField}= :seqName",
				"tableName", tableName, "seqNameField", seqNameField, "seqValueField", seqValueField);
		
		updateSql = exp.parse("update ${tableName} set ${seqValueField} = :newSeqValue where ${seqValueField}=:seqValue and ${seqNameField}=:seqName",
				"tableName", tableName, "seqNameField", seqNameField, "seqValueField", seqValueField);
		
		insertSql = exp.parse("insert into ${tableName} (${seqNameField}, ${seqValueField}) values (:seqName, :seqValue)",
				"tableName", tableName, "seqNameField", seqNameField, "seqValueField", seqValueField);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public long generatedValue(String seqName){
		Number id = selectGeneratedValue(seqName);
		if(id==null){
			insertGeneratedValue(seqName, 1);
			id = selectGeneratedValue(seqName);
		}
		long idresut = id.longValue();
		updateGeneratedValue(seqName, idresut, idresut+1);
		return idresut;
	}
	
	private Number selectGeneratedValue(String seqName){
		Number id = this.getBaseEntityManager().createSQLQuery(selectSql, Number.class)
												.setParameter("seqName", seqName)
												.getSingleResult();
		return id;
	}
	
	private int updateGeneratedValue(String seqName, long seqValue, long newSeqValue){
		return this.getBaseEntityManager().createSQLQuery(updateSql, null)
											.setParameter("seqName", seqName)
											.setParameter("seqValue", seqValue)
											.setParameter("newSeqValue", newSeqValue)
											.executeUpdate();
	}
	
	private int insertGeneratedValue(String seqName, long seqValue){
		return this.getBaseEntityManager().createSQLQuery(insertSql, null)
										.setParameter("seqName", seqName)
										.setParameter("seqValue", seqValue)
										.executeUpdate();
	}

	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	public void setBaseEntityManager(BaseEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
	}

	public String getSeqNameField() {
		return seqNameField;
	}

	public void setSeqNameField(String seqNameField) {
		this.seqNameField = seqNameField;
	}

	public String getSeqValueField() {
		return seqValueField;
	}

	public void setSeqValueField(String seqValueField) {
		this.seqValueField = seqValueField;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
