package org.onetwo.boot.plugins.swagger.service.impl;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import java.util.List;
import java.util.Optional;

import org.onetwo.boot.plugins.swagger.entity.SwaggerEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModuleEntity;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModuleEntity.Status;
import org.onetwo.boot.plugins.swagger.entity.SwaggerModuleEntity.StoreTypes;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

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
	@Autowired
	private SwaggerModelServiceImpl swaggerModelService;
	@Autowired
	private SwaggerParameterServiceImpl swaggerParameterService;
	@Autowired
	private JsonSerializer jsonSerializer;
	
	public Optional<Json> findJsonByGroupName(String groupName){
		/*SwaggerFileEntity fileEntity = findByGroupName(groupName);
		if(fileEntity==null){
			return Optional.empty();
		}
		Optional<Json> json = Optional.of(new Json(fileEntity.getContent()));*/
		Optional<Json> json = convertByGroupName(groupName).map(s->jsonSerializer.toJson(s));
		return json;
	}
	

	public Optional<Swagger> convertByGroupName(String applicationName){
		SwaggerModuleEntity file = findByApplicationName(applicationName);
		if(file==null){
//			throw new BaseException("swagger module not found for: " + groupName);
			return Optional.empty();
		}
		SwaggerEntity swaggerEntity = swaggerService.findByModuleId(file.getId());
		if(swaggerEntity==null){
//			throw new BaseException("swagger not found for swaggerFileId: " + file.getId());
			return Optional.empty();
		}
		Swagger swagger = this.swaggerService.convertBySwagger(swaggerEntity);
		return Optional.ofNullable(swagger);
	}
	
	
	/***
	 * 通过分组名称查找
	 * @author wayshall
	 * @param groupName
	 * @return
	 */
	public SwaggerModuleEntity findByApplicationName(String groupName){
		SwaggerModuleEntity file = baseEntityManager.findOne(SwaggerModuleEntity.class, "applicationName", groupName);
		return file;
	}

	public List<SwaggerModuleEntity> findAllEnabled(){
		return findListByStatus(SwaggerModuleEntity.Status.ENABLED);
	}
	
	public List<SwaggerModuleEntity> findListByStatus(Status status){
		List<SwaggerModuleEntity> files = Querys.from(baseEntityManager, SwaggerModuleEntity.class)
											  .where()
											  	.field("status").equalTo(status)
											  	.ignoreIfNull()
											  .end()
											  .toQuery()
											  .list();
		return files;
	}
	
	public SwaggerModuleEntity saveSwaggerFile(StoreTypes storeType, Swagger swagger, String content){
		String applicationName = swagger.getInfo().getTitle();
		SwaggerModuleEntity swaggerFileEntity = baseEntityManager.findOne(SwaggerModuleEntity.class, "applicationName", applicationName);
		if(swaggerFileEntity==null){
			swaggerFileEntity = new SwaggerModuleEntity();
		}
//		swaggerFileEntity.setGroupName(groupName);
		swaggerFileEntity.setApplicationName(swagger.getInfo().getTitle());
		swaggerFileEntity.setStatus(Status.ENABLED);
		swaggerFileEntity.setStoreType(storeType);
		swaggerFileEntity.setContent(content);
		baseEntityManager.save(swaggerFileEntity);
		return swaggerFileEntity;
	}
	
	public SwaggerModuleEntity importSwagger(MultipartFile swaggerFile){
		String content = SpringUtils.readMultipartFile(swaggerFile);
		return importSwagger(content);
	}
	
	public SwaggerModuleEntity importSwagger(String content){
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
		SwaggerModuleEntity file = saveSwaggerFile(storeType, swagger, content);
		
		this.swaggerService.save(file, swagger);
		return file;
	}

	public SwaggerModuleEntity removeWithCascadeData(String groupName){
		SwaggerModuleEntity swaggerFileEntity = findByApplicationName(groupName);
		return removeWithCascadeData(swaggerFileEntity);
	}
	public SwaggerModuleEntity removeWithCascadeData(Long swaggerFileId){
		SwaggerModuleEntity swaggerFileEntity = baseEntityManager.load(SwaggerModuleEntity.class, swaggerFileId);
		return removeWithCascadeData(swaggerFileEntity);
	}
    public SwaggerModuleEntity removeWithCascadeData(SwaggerModuleEntity swaggerFileEntity){
    	this.swaggerService.removeWithCascadeData(swaggerFileEntity.getId());
    	baseEntityManager.remove(swaggerFileEntity);
    	return swaggerFileEntity;
    }
}
