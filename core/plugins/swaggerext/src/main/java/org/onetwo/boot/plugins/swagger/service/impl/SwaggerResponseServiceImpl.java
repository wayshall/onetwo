
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.plugins.swagger.entity.SwaggerOperationEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerResponseEntity;
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

    public List<SwaggerResponseEntity> save(SwaggerOperationEntity operation, Map<String, Response> responses){
    	this.removeByOperationId(operation.getId());
    	
    	List<SwaggerResponseEntity> responseList = Lists.newArrayList();
    	for(Entry<String, Response> response : responses.entrySet()){
    		SwaggerResponseEntity e = save(operation.getId(), response.getKey(), response.getValue());
    		responseList.add(e);
    	}
    	return responseList;
    }

    public SwaggerResponseEntity save(Long operationId, String code, Response response){
    	SwaggerResponseEntity entity = new SwaggerResponseEntity();
    	entity.setDescription(response.getDescription());
    	entity.setOperationId(operationId);
    	entity.setResponseCode(code);
    	entity.setJsonType(response.getClass().getName());
    	entity.setJsonData(SwaggerUtils.toJson(response));
    	baseEntityManager.save(entity);
    	return entity;
    }

    public int removeByOperationId(Long operationId){
    	int deleteCount = Querys.from(SwaggerResponseEntity.class)
    				 .where()
    				 	.field("operationId").is(operationId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} parameters for operation: {}", deleteCount, operationId);
    	}
    	return deleteCount;
    }
}