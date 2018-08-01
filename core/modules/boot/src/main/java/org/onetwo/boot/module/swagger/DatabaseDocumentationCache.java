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
			fakeDocumentations.put(fe.getFileName(), new FakeDocumentation(fe.getFileName()));
		});
		return Collections.unmodifiableMap(fakeDocumentations);
	}
	
	public static class FakeDocumentation extends Documentation {
		public FakeDocumentation(String groupName) {
			super(groupName, "FakeDocumentation_basePath", Collections.emptySet(), 
					null, null, null,
					Collections.emptySet(), "FakeDocumentation_host", Collections.emptySet(), Collections.emptyList());
		}
		
	}
}
