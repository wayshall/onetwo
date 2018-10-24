
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerResponseEntity;
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
public class SwaggerResponseServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
	@Autowired
	private SwaggerModelMapper swaggerModelMapper;

    public List<SwaggerResponseEntity> save(SwaggerOperationEntity operation, Map<String, Response> responses){
    	this.removeByOperationId(operation.getId());
    	
    	List<SwaggerResponseEntity> responseList = Lists.newArrayList();
    	for(Entry<String, Response> response : responses.entrySet()){
    		SwaggerResponseEntity e = save(operation, response.getKey(), response.getValue());
    		responseList.add(e);
    	}
    	return responseList;
    }

    public SwaggerResponseEntity save(SwaggerOperationEntity operation, String code, Response response){
    	SwaggerResponseEntity entity = new SwaggerResponseEntity();
    	entity.setDescription(response.getDescription());
    	entity.setOperationId(operation.getId());
    	entity.setResponseCode(code);
    	entity.setJsonType(response.getClass().getName());
    	entity.setJsonData(SwaggerUtils.toJson(response));
    	entity.setSwaggerId(operation.getSwaggerId());
    	baseEntityManager.save(entity);
    	return entity;
    }

    public int removeByOperationId(String... operationId){
    	int deleteCount = Querys.from(SwaggerResponseEntity.class)
    				 .where()
    				 	.field("operationId").in(operationId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} parameters for operation: {}", deleteCount, operationId);
    	}
    	return deleteCount;
    }


    public int removeBySwaggerId(Long swaggerId){
    	int deleteCount = Querys.from(SwaggerResponseEntity.class)
    				 .where()
    				 	.field("swaggerId").is(swaggerId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} parameters for swagger: {}", deleteCount, swaggerId);
    	}
    	return deleteCount;
    }
    
    public Map<String, Response> findResponseMapByOperationId(String operationId){
    	List<SwaggerResponseEntity> responseEntities = baseEntityManager.findList(SwaggerResponseEntity.class, "operationId", new String[]{"0", operationId});
		Map<String, Response> responses = responseEntities.stream()
														.collect(Collectors.toMap(r->r.getResponseCode(), r->swaggerModelMapper.map2Response(r)));
		return responses;
    }
}