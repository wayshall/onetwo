package org.onetwo.project.batch.tools.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PsamService {

	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Transactional
	public List<PsamEntity> findByParams(Map<Object, Object> params){
		List<PsamEntity> psamList = this.baseEntityManager.findByProperties(PsamEntity.class, params);
		return psamList;
	}
}
