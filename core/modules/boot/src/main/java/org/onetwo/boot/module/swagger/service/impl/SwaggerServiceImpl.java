
package org.onetwo.boot.module.swagger.service.impl;

import io.swagger.models.Model;
import io.swagger.models.Swagger;

import java.util.Map;

import org.onetwo.boot.module.swagger.entity.SwaggerEntity;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SwaggerServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private SwaggerModelServiceImpl swaggerModelService;
    @Autowired
    private SwaggerOperationServiceImpl swaggerOperationService;
    
    public SwaggerEntity save(Long swaggerFileId, Swagger swagger) {
    	SwaggerEntity swaggerEntity = this.baseEntityManager.findOne(SwaggerEntity.class, "swaggerFileId", swaggerFileId);
    	if(swaggerEntity==null){
    		swaggerEntity = new SwaggerEntity();
    	}
    	swaggerEntity.setBasePath(swagger.getBasePath());
    	swaggerEntity.setHost(swagger.getHost());
    	swaggerEntity.setSwagger(swagger.getSwagger());
    	swaggerEntity.setInfo(swagger.getInfo());
    	swaggerEntity.setSwaggerFileId(swaggerFileId);
    	baseEntityManager.save(swaggerEntity);

    	swaggerModelService.saveDefinitions(swaggerEntity, swagger.getDefinitions());
    	swaggerOperationService.saveOperatioins(swaggerEntity, swagger.getPaths());
    	
    	return swaggerEntity;
	}
    

}