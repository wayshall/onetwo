package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;


public interface FileNamedQueryFactoryListener {

	public void onInitialized(BaseEntityManager em, FileNamedQueryFactory<? extends NamespaceProperty> fq);
}
