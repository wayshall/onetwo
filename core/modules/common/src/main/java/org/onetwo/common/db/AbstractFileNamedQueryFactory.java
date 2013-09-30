package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

abstract public class AbstractFileNamedQueryFactory<T extends NamespaceProperty> implements FileNamedQueryFactory<T> {

	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;

	public AbstractFileNamedQueryFactory(
			FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super();
		this.fileNamedQueryFactoryListener = fileNamedQueryFactoryListener;
	}
	

	public void initQeuryFactory(BaseEntityManager em){
		this.buildNamedQueryInfos();
		if(this.fileNamedQueryFactoryListener!=null)
			this.fileNamedQueryFactoryListener.onInitialized(em, this);
	}
	
	abstract public void buildNamedQueryInfos();
	
}
