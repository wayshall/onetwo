
package org.onetwo.boot.module.swagger.service.impl;

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.onetwo.boot.module.swagger.entity.SwaggerOperationEntity;

@Service
@Transactional
public class SwaggerOperationServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<SwaggerOperationEntity> findPage(Page<SwaggerOperationEntity> page, SwaggerOperationEntity swaggerOperation){
        return Querys.from(baseEntityManager, SwaggerOperationEntity.class)
                	.where()
            		  .addFields(swaggerOperation)
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public void save(SwaggerOperationEntity entity) {
		baseEntityManager.persist(entity);
	}

	public void update(SwaggerOperationEntity entity) {
		baseEntityManager.update(entity);
	}
    
    public SwaggerOperationEntity findById(Long id) {
		return baseEntityManager.findById(SwaggerOperationEntity.class, id);
	}

	public Collection<SwaggerOperationEntity> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(SwaggerOperationEntity.class, id);
	}
}