package org.onetwo.plugins.codegen.model.service.impl;
import java.io.Serializable;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.entity.DatabaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DatabaseServiceImpl {
	
	final private Class<DatabaseEntity> entityClass = DatabaseEntity.class;
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;
	
	
	public DatabaseEntity load(Long id) {
		DatabaseEntity db = null;
		if(id==this.tableManagerFactory.getDefaultDataBase().getId()){
			db = this.tableManagerFactory.getDefaultDataBase();
		}else{
			db = baseEntityManager.load(DatabaseEntity.class, id);
		}
		return db;
	}


	public DatabaseEntity findById(Serializable id) {
		return baseEntityManager.findById(entityClass, id);
	}


	public DatabaseEntity save(DatabaseEntity entity) {
		return baseEntityManager.save(entity);
	}


	public DatabaseEntity removeById(Serializable id) {
		return baseEntityManager.removeById(entityClass, id);
	}


	public void findPage(Page<DatabaseEntity> page, Object... properties) {
		baseEntityManager.findPage(entityClass, page, properties);
	}
	
	
}
