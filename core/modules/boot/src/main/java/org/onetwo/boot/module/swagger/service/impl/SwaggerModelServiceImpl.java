
package org.onetwo.boot.module.swagger.service.impl;

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.onetwo.boot.module.swagger.entity.SwaggerModelEntity;

@Service
@Transactional
public class SwaggerModelServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<SwaggerModelEntity> findPage(Page<SwaggerModelEntity> page, SwaggerModelEntity swaggerModel){
        return Querys.from(baseEntityManager, SwaggerModelEntity.class)
                	.where()
            		  .addFields(swaggerModel)
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public void save(SwaggerModelEntity entity) {
		baseEntityManager.persist(entity);
	}

	public void update(SwaggerModelEntity entity) {
		baseEntityManager.update(entity);
	}
    
    public SwaggerModelEntity findById(Long id) {
		return baseEntityManager.findById(SwaggerModelEntity.class, id);
	}

	public Collection<SwaggerModelEntity> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(SwaggerModelEntity.class, id);
	}
}