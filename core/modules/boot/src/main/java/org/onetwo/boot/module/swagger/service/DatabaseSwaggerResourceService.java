package org.onetwo.boot.module.swagger.service;

import java.util.List;

import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
public class DatabaseSwaggerResourceService {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	/***
	 * 通过分组名称查找
	 * @author wayshall
	 * @param groupName
	 * @return
	 */
	public SwaggerFileEntity findByGroupName(String groupName){
		SwaggerFileEntity file = baseEntityManager.findOne(SwaggerFileEntity.class, "fileName", groupName);
		return file;
	}

	public List<SwaggerFileEntity> findAllEnabled(){
		List<SwaggerFileEntity> files = Querys.from(baseEntityManager, SwaggerFileEntity.class)
											  .where()
											  	.field("status").equalTo(SwaggerFileEntity.Status.ENABLED)
											  .end()
											  .toQuery()
											  .list();
		return files;
	}

}
