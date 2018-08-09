
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Swagger;

import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerFileEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class SwaggerServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private SwaggerModelServiceImpl swaggerModelService;
    @Autowired
    private SwaggerOperationServiceImpl swaggerOperationService;
    @Autowired
    private SwaggerParameterServiceImpl swaggerParameterService;
    @Autowired
    private SwaggerResponseServiceImpl swaggerResponseService;
    
    public SwaggerEntity save(SwaggerFileEntity swaggerFile, Swagger swagger) {
    	Assert.notNull(swaggerFile.getId(), "swaggerFile.id can not be null");
    	SwaggerEntity swaggerEntity = findBySwaggerFileId(swaggerFile.getId());
    	if(swaggerEntity==null){
    		swaggerEntity = new SwaggerEntity();
    	}
    	swaggerEntity.setGroupName(swaggerFile.getGroupName());
    	swaggerEntity.setBasePath(swagger.getBasePath());
    	swaggerEntity.setHost(swagger.getHost());
    	swaggerEntity.setSwagger(swagger.getSwagger());
    	swaggerEntity.setInfo(swagger.getInfo());
    	swaggerEntity.setSwaggerFileId(swaggerFile.getId());
    	baseEntityManager.save(swaggerEntity);

    	swaggerModelService.saveDefinitions(swaggerEntity, swagger.getDefinitions());
    	swaggerOperationService.saveOperatioins(swaggerEntity, swagger.getPaths());
    	
    	return swaggerEntity;
	}
    
    public void removeWithCascadeData(Long swaggerFileId){
    	SwaggerEntity swaggerEntity = findBySwaggerFileId(swaggerFileId);
    	Assert.notNull(swaggerEntity, "swagger not found for file: " + swaggerFileId);
		//operation
    	List<SwaggerOperationEntity> operationEntities = this.swaggerOperationService.findListBySwaggerId(swaggerEntity.getId());
    	Long[] operationIds = operationEntities.stream().map(e->e.getId()).collect(Collectors.toList()).toArray(new Long[0]);
    	//parameter
    	this.swaggerParameterService.removeByOperationId(operationIds);
		//response
    	this.swaggerResponseService.removeByOperationId(operationIds);
    	//model
    	swaggerModelService.removeBySwaggerId(swaggerEntity.getId());
    	//operation
    	baseEntityManager.removes(operationEntities);
    	//swagger
    	baseEntityManager.remove(swaggerEntity);
    }
    
    public SwaggerEntity findBySwaggerFileId(Long swaggerFileId){
    	SwaggerEntity swaggerEntity = this.baseEntityManager.findOne(SwaggerEntity.class, "swaggerFileId", swaggerFileId);
    	return swaggerEntity;
    }
    

}