package org.onetwo.common.dbm.model.service;

import org.onetwo.common.dbm.model.entity.CompanyEntity;
import org.onetwo.dbm.core.internal.DbmCrudServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanySerivceImpl extends DbmCrudServiceImpl<CompanyEntity, Long>{
	
	public CompanyEntity findByName(String name){
		CompanyEntity dbCompany = findOne("name", name);
		dbCompany = findOne("name", name);
		dbCompany = findOne("name", name);
		dbCompany = findOne("name", name);
		return dbCompany;
	}
}
