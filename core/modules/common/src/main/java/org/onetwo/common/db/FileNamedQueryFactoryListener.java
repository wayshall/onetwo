package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;


public interface FileNamedQueryFactoryListener<T extends NamespaceProperty> {

	public void onInitialized(BaseEntityManager em, FileNamedQueryFactory<T> fq);
}
