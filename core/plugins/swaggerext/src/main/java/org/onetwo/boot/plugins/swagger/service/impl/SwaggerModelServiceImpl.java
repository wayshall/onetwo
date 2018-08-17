
package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModelEntity;
import org.onetwo.boot.plugins.swagger.mapper.SwaggerModelMapper;
import org.onetwo.boot.plugins.swagger.util.SwaggerUtils;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

@Service
@Transactional
@Slf4j
public class SwaggerModelServiceImpl {

    @Autowired
    private BaseEntityManager baseEntityManager;
	@Autowired
	private SwaggerModelMapper swaggerModelMapper;
	
	public Map<String, Model> convertBySwagger(SwaggerEntity swaggerEntity){
		Assert.notNull(swaggerEntity, "swaggerEntity can not be null");
		List<SwaggerModelEntity> models = findListBySwaggerId(swaggerEntity.getId());
		Map<String, Model> modelMap = models.stream().collect(Collectors.toMap(m->m.getName(), m->swaggerModelMapper.map2Model(m)));
		return modelMap;
	}
    
    public List<SwaggerModelEntity> saveDefinitions(SwaggerEntity swaggerEntity, Map<String, Model> definitions){
    	Assert.notNull(swaggerEntity.getId(), "swaggerEntity.id can not be null");
    	this.removeBySwaggerId(swaggerEntity.getId());
    	
    	List<SwaggerModelEntity> list = Lists.newArrayList();
    	for(Entry<String, Model> entry : definitions.entrySet()){
    		SwaggerModelEntity entity = save(swaggerEntity, entry.getKey(), entry.getValue());
    		list.add(entity);
    	}
    	return list;
    }
    
    public SwaggerModelEntity save(SwaggerEntity swaggerEntity, String name, Model model){
    	SwaggerModelEntity swaggerModelEntity = new SwaggerModelEntity();
    	swaggerModelEntity.setSwaggerId(swaggerEntity.getId());
    	swaggerModelEntity.setDescription(model.getDescription());
    	swaggerModelEntity.setName(name);
    	swaggerModelEntity.setRefPath(SwaggerUtils.getModelRefPath(name));
    	swaggerModelEntity.setJsonType(model.getClass().getName());
    	swaggerModelEntity.setJsonData(SwaggerUtils.toJson(model));
    	swaggerModelEntity.setModuleId(swaggerEntity.getModuleId());
    	baseEntityManager.save(swaggerModelEntity);
    	return swaggerModelEntity;
    }
    
    public int removeBySwaggerId(Long swaggerId){
    	int deleteCount = Querys.from(SwaggerModelEntity.class)
    				 .where()
    				 	.field("swaggerId").in(swaggerId)
    				 .end()
    				 .delete();
    	if(log.isInfoEnabled()){
    		log.info("remove {} models for swagger: {}", deleteCount, swaggerId);
    	}
    	return deleteCount;
    }
    
    public List<SwaggerModelEntity> findListBySwaggerId(Long swaggerId){
    	List<SwaggerModelEntity> models = Querys.from(SwaggerModelEntity.class)
    				 .where()
    				 	.field("swaggerId").is(swaggerId)
    				 .end()
    				 .toQuery()
    				 .list();
    	return models;
    }
    
}