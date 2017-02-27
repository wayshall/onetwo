package org.onetwo.common.dbm.model.dao;

import java.util.List;

import org.onetwo.common.dbm.model.vo.DepartmentVO;
import org.onetwo.dbm.annotation.DbmNestedResult;
import org.onetwo.dbm.annotation.DbmNestedResult.NestedType;
import org.onetwo.dbm.annotation.DbmResultMapping;

public interface CompanyDao {
	
	/*@DbmCascadeResult
	List<CompanyVO> findNestedCompanies();*/
	
	@DbmResultMapping(value={
			@DbmNestedResult(property="company", id="id", columnPrefix="comp_", nestedType=NestedType.ASSOCIATION)
	})
	List<DepartmentVO> findDepartmentsWithComapny();

	@DbmResultMapping(value={
			@DbmNestedResult(property="employees", columnPrefix="emply_", nestedType=NestedType.COLLECTION),
			@DbmNestedResult(property="company", id="id", columnPrefix="comp_", nestedType=NestedType.ASSOCIATION)
	})
	List<DepartmentVO> findNestedDepartments();

	@DbmResultMapping(value={
			@DbmNestedResult(property="employees", id="id", columnPrefix="emply_", nestedType=NestedType.COLLECTION),
			@DbmNestedResult(property="company", id="id", columnPrefix="comp_", nestedType=NestedType.ASSOCIATION)
	})
	List<DepartmentVO> findNestedDepartmentsWithEmployeeId();

}
