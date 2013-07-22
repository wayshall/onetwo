package org.onetwo.plugins.fmtag.service.impl;

import java.util.List;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.fmtag.service.JFishDataDelegateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class JFishDataDelegateServiceImpl implements JFishDataDelegateService  {

	@Override
	public <T> List<T> findListByQName(String queryName, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Page<T> findPage(Class<T> entityClass, Page<T> page,
			Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Resource
//	private JFishEntityManager jfishEntityManager; 
//	
//	@Resource
//	private MappedEntryManager mappedEntryManager;
//
//	@Override
//	public <T> List<T> findListByQName(String queryName, Object... params) {
//		return jfishEntityManager.createJFishQueryByQName(queryName, params).getResultList();
//	}
//	
//	public <T> Page<T> findPage(Class<T> entityClass, Page<T> page, Object...params){
//		jfishEntityManager.findPage(entityClass, page, params);
//		return page;
//	}
//	
//	public JFishMappedEntry getJFishMappedEntry(Object clsName){
//		return this.mappedEntryManager.getEntry(clsName);
//	}
	
}
