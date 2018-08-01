
package org.onetwo.boot.module.swagger.service.impl;

import java.util.Collection;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.onetwo.boot.module.swagger.entity.SwaggerParameterEntity;

@Service
@Transactional
public class SwaggerParameterServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    
    public Page<SwaggerParameterEntity> findPage(Page<SwaggerParameterEntity> page, SwaggerParameterEntity swaggerParameter){
        return Querys.from(baseEntityManager, SwaggerParameterEntity.class)
                	.where()
            		  .addFields(swaggerParameter)
            		  .ignoreIfNull()
            		.end()
            		.toQuery()
            		.page(page);
    }
    
    public void save(SwaggerParameterEntity entity) {
		baseEntityManager.persist(entity);
	}

	public void update(SwaggerParameterEntity entity) {
		baseEntityManager.update(entity);
	}
    
    public SwaggerParameterEntity findById(Long id) {
		return baseEntityManager.findById(SwaggerParameterEntity.class, id);
	}

	public Collection<SwaggerParameterEntity> removeByIds(Long... id) {
		return baseEntityManager.removeByIds(SwaggerParameterEntity.class, id);
	}
}