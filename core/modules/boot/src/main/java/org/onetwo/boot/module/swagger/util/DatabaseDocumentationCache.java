package org.onetwo.boot.module.swagger;

import java.util.Collections;
import java.util.Map;

import org.onetwo.boot.module.swagger.service.impl.DatabaseSwaggerResourceService;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;

import com.google.common.collect.Maps;

/**
 * @author wayshall <br/>
 */
public class DatabaseDocumentationCache extends DocumentationCache {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private DatabaseSwaggerResourceService databaseSwaggerResourceService;

	
	/***
	 * for groupName
	 */
	@Override
	public Map<String, Documentation> all() {
		Map<String, Documentation> fakeDocumentations = Maps.newLinkedHashMap();
		fakeDocumentations.putAll(super.all());
		databaseSwaggerResourceService.findAllEnabled().forEach(fe->{
			fakeDocumentations.put(fe.getGroupName(), new DatabaseDocumentation(fe.getId(), fe.getGroupName()));
		});
		return Collections.unmodifiableMap(fakeDocumentations);
	}
	
	public static class DatabaseDocumentation extends Documentation {
		private Long moduleId;
		public DatabaseDocumentation(Long moduleId, String groupName) {
			super(groupName, "FakeDocumentation_basePath", Collections.emptySet(), 
					null, null, null,
					Collections.emptySet(), "FakeDocumentation_host", Collections.emptySet(), Collections.emptyList());
			this.moduleId = moduleId;
		}
		public Long getModuleId() {
			return moduleId;
		}
		public void setModuleId(Long moduleId) {
			this.moduleId = moduleId;
		}
		
	}
}
