package org.onetwo.boot.plugins.swagger.util;

import java.util.Collections;
import java.util.Map;

import org.onetwo.boot.plugins.swagger.service.impl.DatabaseSwaggerResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;

/**
 * @author wayshall <br/>
 */
public class DatabaseDocumentationCache extends DocumentationCache {
	
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
			fakeDocumentations.put(fe.getModuleName(), new DatabaseDocumentation(fe.getId(), fe.getModuleName()));
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
