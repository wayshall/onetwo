package org.onetwo.dbm.ui.spi;

import java.util.Optional;

import org.onetwo.dbm.ui.meta.DUIEntityMeta;

/**
 * @author weishao zeng
 * <br/>
 */

public interface DUIMetaManager {

	DUIEntityMeta getByTable(String tableName);
	DUIEntityMeta get(String entityName);
	DUIEntityMeta get(Class<?> entityClass);
	
	Optional<DUIEntityMeta> find(Class<?> uiEntityClass);
}
