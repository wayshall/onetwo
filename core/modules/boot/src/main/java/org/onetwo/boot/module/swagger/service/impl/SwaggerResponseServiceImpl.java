
package org.onetwo.boot.module.swagger.service.impl;

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.onetwo.boot.module.swagger.entity.SwaggerResponseEntity;

@Service
@Transactional
public class SwaggerResponseServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<SwaggerResponseEntity> findPage(Page<SwaggerResponseEntity> page, SwaggerResponseEntity swaggerResponse){
        return Querys.from(baseEntityManager, SwaggerResponseEntity.class)
                	.where()
            		  .addFields(swaggerResponse)
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public void save(SwaggerResponseEntity entity) {
		baseEntityManager.persist(entity);
	}

	public void update(SwaggerResponseEntity entity) {
		baseEntityManager.update(entity);
	}
    
    public SwaggerResponseEntity findById(Long id) {
		return baseEntityManager.findById(SwaggerResponseEntity.class, id);
	}

	public Collection<SwaggerResponseEntity> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(SwaggerResponseEntity.class, id);
	}
}