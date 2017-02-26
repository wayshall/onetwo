package org.onetwo.common.dbm.model.dao;

import java.util.List;

import org.onetwo.common.dbm.model.vo.CompanyVO;
import org.onetwo.dbm.annotation.DbmCascadeResult;

public interface CompanyDao {
	
	@DbmCascadeResult
	List<CompanyVO> findNestedCompanies();

}
