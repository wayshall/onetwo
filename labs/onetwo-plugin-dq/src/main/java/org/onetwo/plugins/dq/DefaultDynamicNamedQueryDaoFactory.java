package org.onetwo.plugins.dq;

import org.onetwo.common.fish.spring.JFishDao;
import org.onetwo.common.fish.spring.JFishDaoLifeCycleListener;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManager;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.dq.annotations.QueryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

public class DefaultDynamicNamedQueryDaoFactory implements JFishDaoLifeCycleListener {

	private String packageToScan;
	
	private ResourcesScanner scanner = new JFishResourcesScanner();
	
	@Autowired
	private JFishNamedFileQueryManager jfishNamedFileQueryManager;
	
	@Override
	public void onInit(JFishDao dao) {
		Assert.hasText(packageToScan);
		this.scanner.scan(new ScanResourcesCallback<Class<?>>() {

			@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				return metadataReader.getAnnotationMetadata().hasAnnotation(QueryInterface.class.getName());
			}

			@Override
			public Class<?> doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
				return ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
			}
			
		}, packageToScan);
	}

	@Override
	public void onDestroy(JFishDao dao) {
		
	}

	public String getPackageToScan() {
		return packageToScan;
	}

	public void setPackageToScan(String packageToScan) {
		this.packageToScan = packageToScan;
	}

}
