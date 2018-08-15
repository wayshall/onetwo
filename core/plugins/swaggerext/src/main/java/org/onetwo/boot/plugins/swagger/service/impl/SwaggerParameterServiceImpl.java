
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.parameters.Parameter;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerParameterEntity;
import org.onetwo.boot.plugins.swagger.mapper.SwaggerModelMapper;
import org.onetwo.boot.plugins.swagger.util.SwaggerUtils;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
@Slf4j
public class SwaggerParameterServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
	@Autowired
	private SwaggerModelMapper swaggerModelMapper;

    public List<SwaggerParameterEntity> save(SwaggerOperationEntity operation, List<Parameter> parameters){
    	this.removeByOperationId(operation.getId());
    	
    	List<SwaggerParameterEntity> operations = Lists.newArrayList();
    	for(Parameter parameter : parameters){
    		SwaggerParameterEntity e = save(operation, parameter);
    		operations.add(e);
    	}
    	return operations;
    }

    public SwaggerParameterEntity save(SwaggerOperationEntity operation, Parameter parameter){
    	SwaggerParameterEntity entity = new SwaggerParameterEntity();
    	entity.setDescription(parameter.getDescription());
    	entity.setName(parameter.getName());
    	entity.setOperationId(operation.getId());
    	entity.setJsonType(parameter.getClass().getName());
    	entity.setJsonData(SwaggerUtils.toJson(parameter));
    	entity.setSwaggerId(operation.getSwaggerId());
    	baseEntityManager.save(entity);
    	return entity;
    }

    public int removeByOperationId(String... operationId){
    	int deleteCount = Querys.from(SwaggerParameterEntity.class)
    				 .where()
    				 	.field("operationId").in(operationId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} parameters for operation: {}", deleteCount, operationId);
    	}
    	return deleteCount;
    }
    
    public List<Parameter> findParametersByOperationId(String operationId){
    	List<SwaggerParameterEntity> paramEntities = baseEntityManager.findList(SwaggerParameterEntity.class, "operationId", new String[]{"0", operationId});
		List<Parameter> parameters = paramEntities.stream().map(p->{
			return swaggerModelMapper.map2Parameter(p);
		})
		.collect(Collectors.toList());
		return parameters;
    }
}