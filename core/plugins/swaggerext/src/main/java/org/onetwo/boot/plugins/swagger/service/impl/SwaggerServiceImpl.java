
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModuleEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.mapper.SwaggerModelMapper;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

@Service
@Transactional
public class SwaggerServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private SwaggerModelServiceImpl swaggerModelService;
	@Autowired
	private SwaggerModelMapper swaggerModelMapper;
    @Autowired
	private SwaggerOperationServiceImpl swaggerOperationService;
    @Autowired
    private SwaggerParameterServiceImpl swaggerParameterService;
    @Autowired
    private SwaggerResponseServiceImpl swaggerResponseService;
    

	public Swagger convertBySwagger(SwaggerEntity swaggerEntity){
		Swagger swagger = swaggerModelMapper.map2Swagger(swaggerEntity);
		//operations, paramters, responses
		Map<String, Path> paths = swaggerOperationService.convertBySwagger(swaggerEntity);
		swagger.setPaths(paths);
		//model
		Map<String, Model> definitions = this.swaggerModelService.convertBySwagger(swaggerEntity);
		swagger.setDefinitions(definitions);
		//tags
		Set<Tag> tags = paths.values().stream().flatMap(path->{
			return path.getOperations().stream();
		})
		.filter(op->op.getTags()!=null)
		.flatMap(op->{
			return op.getTags().stream();
		})
		.map(tag->new Tag().name(tag))
		.collect(Collectors.toSet());
		
		swagger.setTags(Lists.newArrayList(tags));
		
		return swagger;
	}
	
    
    public SwaggerEntity save(SwaggerModuleEntity module, Swagger swagger) {
    	Assert.notNull(module.getId(), "swaggerFile.id can not be null");
    	SwaggerEntity swaggerEntity = findByModuleId(module.getId());
    	if(swaggerEntity==null){
    		swaggerEntity = new SwaggerEntity();
    		swaggerEntity.setUpdateCount(1);
    	}else{
    		swaggerEntity.setUpdateCount(swaggerEntity.getUpdateCount()+1);
    	}
    	swaggerEntity.setApplicationName(module.getApplicationName());
    	swaggerEntity.setBasePath(swagger.getBasePath());
    	swaggerEntity.setHost(swagger.getHost());
    	swaggerEntity.setSwagger(swagger.getSwagger());
    	swaggerEntity.setInfo(swagger.getInfo());
    	swaggerEntity.setModuleId(module.getId());
    	baseEntityManager.save(swaggerEntity);

    	swaggerModelService.saveDefinitions(swaggerEntity, swagger.getDefinitions());
    	swaggerOperationService.saveOperatioins(swaggerEntity, swagger.getPaths());
    	
    	return swaggerEntity;
	}
    
    public void removeWithCascadeData(Long moduleId){
    	SwaggerEntity swaggerEntity = findByModuleId(moduleId);
    	Assert.notNull(swaggerEntity, "swagger not found for module: " + moduleId);
		//operation
    	List<SwaggerOperationEntity> operationEntities = this.swaggerOperationService.findListBySwaggerId(swaggerEntity.getId());
    	String[] operationIds = operationEntities.stream().map(e->e.getId()).collect(Collectors.toList()).toArray(new String[0]);
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
    
    public SwaggerEntity findByModuleId(Long moduleId){
    	SwaggerEntity swaggerEntity = this.baseEntityManager.findOne(SwaggerEntity.class, "moduleId", moduleId);
    	return swaggerEntity;
    }
    

}