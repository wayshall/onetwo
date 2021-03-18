package org.onetwo.dbm.ui;

import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.ui.core.DefaultDUIMetaManager;
import org.onetwo.dbm.ui.core.DefaultUIEntityMetaService;
import org.onetwo.dbm.ui.core.DefaultUISelectDataProviderService;
import org.onetwo.dbm.ui.json.DUIObjectMapperCustomizer;
import org.onetwo.dbm.ui.json.ObjectNodeToStringValueWriter;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.onetwo.dbm.ui.spi.DUISelectDataProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
public class DbmUIConfiguration implements ImportAware {
	
	private String[] packagesToScan;
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		if(importMetadata==null){
			return ;
		}
		Map<String, Object> annotationAttributes = importMetadata.getAnnotationAttributes(EnableDbmUI.class.getName());
		if(annotationAttributes!=null){
			this.packagesToScan = (String[])annotationAttributes.get("packagesToScan");
		}
		if (LangUtils.isEmpty(packagesToScan)) {
			String importingAnnotationClassName = importMetadata.getClassName();
			String packName = ReflectUtils.loadClass(importingAnnotationClassName).getPackage().getName();
			this.packagesToScan = new String[] {packName};
		}
	}

	@Bean
	public DUIMetaManager duiClassMetaManager() {
		DefaultDUIMetaManager metaManager = new DefaultDUIMetaManager();
		metaManager.setPackagesToScan(packagesToScan);
		return metaManager;
	}
	
	@Bean
	public DUISelectDataProviderService duiSelectDataProviderService() {
		return new DefaultUISelectDataProviderService();
	}
	
	@Bean
	public DefaultUIEntityMetaService duiEntityMetaService() {
		return new DefaultUIEntityMetaService();
	}
	
	@Bean
	public DUIObjectMapperCustomizer duiObjectMapperCustomizer() {
		return new DUIObjectMapperCustomizer();
	}
	
	@Bean
	public ObjectNodeToStringValueWriter stringValueWriter() {
		return new ObjectNodeToStringValueWriter();
	}
}
