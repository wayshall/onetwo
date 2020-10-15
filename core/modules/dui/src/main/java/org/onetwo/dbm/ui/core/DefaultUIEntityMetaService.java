package org.onetwo.dbm.ui.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.onetwo.dbm.ui.vo.QueryOperators;
import org.onetwo.dbm.ui.vo.SearchableFieldVO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author weishao zeng
 * <br/>
 */
public class DefaultUIEntityMetaService {
	
	@Autowired
	private DUIMetaManager uiclassMetaManager;
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	public List<SearchableFieldVO> getSearchFields(String entityName) {
		DUIEntityMeta meta = uiclassMetaManager.get(entityName);
		Collection<DUIFieldMeta> searchFields = meta.getSearchableFields();
		
		List<SearchableFieldVO> searchFieldList = searchFields.stream().map(fieldMeta -> {
			SearchableFieldVO field = new SearchableFieldVO();
			field.setLabel(fieldMeta.getLabel());
			field.setName(field.getName());
			field.addOperator(QueryOperators.EQUAL);
			return field;
		}).collect(Collectors.toList());
		return searchFieldList;
	}
}
