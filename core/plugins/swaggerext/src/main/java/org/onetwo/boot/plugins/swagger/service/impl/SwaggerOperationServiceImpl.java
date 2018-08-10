
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.mapper.SwaggerModelMapper;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;

@Service
@Transactional
@Slf4j
public class SwaggerOperationServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
    @Autowired
    private SwaggerParameterServiceImpl swaggerParameterService;
    @Autowired
    private SwaggerResponseServiceImpl swaggerResponseService;
	@Autowired
	private SwaggerModelMapper swaggerModelMapper;

    public List<SwaggerOperationEntity> saveOperatioins(SwaggerEntity swaggerEntity, Map<String, Path> definitions){
    	this.removeBySwaggerId(swaggerEntity.getId());
    	
    	List<SwaggerOperationEntity> operations = Lists.newArrayList();
    	for(Entry<String, Path> entry : definitions.entrySet()){
    		Path path = entry.getValue();
    		save(swaggerEntity, entry.getKey(), path);
    	}
    	return operations;
    }

    public List<SwaggerOperationEntity> save(SwaggerEntity swaggerEntity, String path, Path pathObject){
    	List<SwaggerOperationEntity> operations = Lists.newArrayList();
		save(swaggerEntity, path, RequestMethod.GET, pathObject.getGet()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.PUT, pathObject.getPut()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.POST, pathObject.getPost()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.HEAD, pathObject.getHead()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.DELETE, pathObject.getDelete()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.PATCH, pathObject.getPatch()).ifPresent(entity->operations.add(entity));
		save(swaggerEntity, path, RequestMethod.OPTIONS, pathObject.getOptions()).ifPresent(entity->operations.add(entity));
    	return operations;
    }
    
    public Optional<SwaggerOperationEntity> save(SwaggerEntity swaggerEntity, String path, RequestMethod method, Operation operation){
    	if(operation==null){
    		return Optional.empty();
    	}
    	SwaggerOperationEntity operationEntity = new SwaggerOperationEntity();
    	operationEntity.setSwaggerId(swaggerEntity.getId());
    	operationEntity.setSummary(operation.getSummary());
    	operationEntity.setDeprecated(operation.isDeprecated());
    	operationEntity.setConsumes(operation.getConsumes());
    	operationEntity.setProduces(operation.getProduces());
    	operationEntity.setRequestMethod(method.name());
    	operationEntity.setSchemes(operation.getSchemes());
    	operationEntity.setExternaldocs(operation.getExternalDocs());
    	operationEntity.setTags(operation.getTags());
    	operationEntity.setPath(path);
    	operationEntity.setSwaggerFileId(swaggerEntity.getSwaggerFileId());
    	operationEntity.setDescription(operation.getDescription());
    	baseEntityManager.save(operationEntity);
    	
    	swaggerParameterService.save(operationEntity, operation.getParameters());
    	swaggerResponseService.save(operationEntity, operation.getResponses());
    	
    	return Optional.of(operationEntity);
    }
    
    public int removeBySwaggerId(Long swaggerId){
    	int deleteCount = Querys.from(SwaggerOperationEntity.class)
    				 .where()
    				 	.field("swaggerId").is(swaggerId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} operations for swagger: {}", deleteCount, swaggerId);
    	}
    	return deleteCount;
    }
    
    public List<SwaggerOperationEntity> findListBySwaggerId(Long swaggerId){
    	return Querys.from(SwaggerOperationEntity.class)
					 .where()
					 	.field("swaggerId").is(swaggerId)
					 .end()
					 .toQuery()
					 .list();
    }
    
	
	public Map<String, Path> convertBySwagger(SwaggerEntity swaggerEntity){
		Assert.notNull(swaggerEntity, "swaggerEntity can not be null");
		List<SwaggerOperationEntity> operations = findListBySwaggerId(swaggerEntity.getId());
		//paramters, responses
		Map<String, Path> paths = this.swaggerModelMapper.map2Paths(operations, (opEntity, op)->{
			List<Parameter> parameters = swaggerParameterService.findParametersByOperationId(opEntity.getId());
			op.setParameters(parameters);
			Map<String, Response> responses = swaggerResponseService.findResponseMapByOperationId(opEntity.getId());
			op.setResponses(responses);
		});
		return paths;
	}
}