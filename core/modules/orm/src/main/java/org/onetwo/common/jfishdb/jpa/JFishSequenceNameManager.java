package org.onetwo.common.jfishdb.jpa;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;

/***
 * 序列管理类
 * @author weishao
 *
 */
public class JFishSequenceNameManager extends SequenceNameManager {
	

//	protected SQLFile sqlFile = SQLFile.getInstance();
	
	public <T> String getSequenceName(Class<T> entityClass){
		
		String seqName = null;
		SequenceGenerator seq = entityClass.getAnnotation(SequenceGenerator.class);
		if(seq!=null){
			seqName = seq.sequenceName();
			if(StringUtils.isBlank(seqName))
				seqName = seq.name();
		}
		if(StringUtils.isBlank(seqName)){
			Table table = entityClass.getAnnotation(Table.class);
			if(table!=null)
				seqName = "SEQ_" + table.name();
		}
		if(StringUtils.isBlank(seqName))
			throw new ServiceException("can not find the sequence. class["+entityClass.getName()+"]");
		
		return seqName;
	}
	
}
