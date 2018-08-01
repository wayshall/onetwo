package org.onetwo.boot.module.swagger.service.impl;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import java.util.List;

import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity;
import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity.Status;
import org.onetwo.boot.module.swagger.entity.SwaggerFileEntity.StoreTypes;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wayshall
 * <br/>
 */
@Transactional
@Service
public class DatabaseSwaggerResourceService {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private SwaggerServiceImpl swaggerService;
	
	
	/***
	 * 通过分组名称查找
	 * @author wayshall
	 * @param groupName
	 * @return
	 */
	public SwaggerFileEntity findByGroupName(String groupName){
		SwaggerFileEntity file = baseEntityManager.findOne(SwaggerFileEntity.class, "fileName", groupName);
		return file;
	}

	public List<SwaggerFileEntity> findAllEnabled(){
		List<SwaggerFileEntity> files = Querys.from(baseEntityManager, SwaggerFileEntity.class)
											  .where()
											  	.field("status").equalTo(SwaggerFileEntity.Status.ENABLED)
											  .end()
											  .toQuery()
											  .list();
		return files;
	}
	
	public SwaggerFileEntity saveSwaggerFile(StoreTypes storeType, Swagger swagger, String groupName, String content){
		SwaggerFileEntity swaggerFileEntity = baseEntityManager.findOne(SwaggerFileEntity.class, "fileName", groupName);
		if(swaggerFileEntity==null){
			swaggerFileEntity = new SwaggerFileEntity();
		}
		swaggerFileEntity.setFileName(groupName);
		swaggerFileEntity.setApplicationName(swagger.getInfo().getTitle());
		swaggerFileEntity.setStatus(Status.ENABLED);
		swaggerFileEntity.setStoreType(storeType);
		swaggerFileEntity.setContent(content);
		baseEntityManager.save(swaggerFileEntity);
		return swaggerFileEntity;
	}
	
	public SwaggerFileEntity importSwagger(MultipartFile swaggerFile){
		String content = SpringUtils.readMultipartFile(swaggerFile);
		return importSwagger(swaggerFile.getOriginalFilename(), content);
	}
	
	public SwaggerFileEntity importSwagger(String groupName, String content){
		StoreTypes storeType;
		Swagger swagger;
		SwaggerParser parser = new SwaggerParser();
		if(content.startsWith("http")){
			storeType = StoreTypes.URL;
			swagger = parser.read(content);
		}else if(content.startsWith("file:")){
			storeType = StoreTypes.DATA;
			swagger = parser.read(content);
		}else{
			storeType = StoreTypes.DATA;
			swagger = parser.parse(content);
		}
		SwaggerFileEntity file = saveSwaggerFile(storeType, swagger, groupName, content);
		
		this.swaggerService.save(file, swagger);
		return file;
	}

}
