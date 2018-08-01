
package org.onetwo.boot.module.swagger.service.impl;

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.onetwo.boot.module.swagger.entity.SwaggerEntity;

@Service
@Transactional
public class SwaggerServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<SwaggerEntity> findPage(Page<SwaggerEntity> page, SwaggerEntity swagger){
        return Querys.from(baseEntityManager, SwaggerEntity.class)
                	.where()
            		  .addFields(swagger)
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public void save(SwaggerEntity entity) {
		baseEntityManager.persist(entity);
	}

	public void update(SwaggerEntity entity) {
		baseEntityManager.update(entity);
	}
    
    public SwaggerEntity findById(Long id) {
		return baseEntityManager.findById(SwaggerEntity.class, id);
	}

	public Collection<SwaggerEntity> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(SwaggerEntity.class, id);
	}
}