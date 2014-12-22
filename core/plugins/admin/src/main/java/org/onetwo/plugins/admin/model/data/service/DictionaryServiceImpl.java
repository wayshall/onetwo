package org.onetwo.plugins.admin.model.data.service;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity.DicType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DictionaryServiceImpl extends HibernateCrudServiceImpl<DictionaryEntity, Long> {
	
	public void findDictionaryTypePage(Page<DictionaryEntity> page){
		findPage(page, "dictType", DicType.TYPE, K.DESC, "id");
	}

}