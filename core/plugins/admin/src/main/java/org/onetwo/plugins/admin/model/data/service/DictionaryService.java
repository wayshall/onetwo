package org.onetwo.plugins.admin.model.data.service;

import java.util.List;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.vo.DictTypeVo;

public interface DictionaryService {
	public List<DictionaryEntity> findByPrefixCode(String code);
	public DictionaryEntity findByCode(String code);
	public int importDatas(List<DictTypeVo> datas);
	public DictionaryEntity findById(Long id);

	public DictionaryEntity removeById(Long id);

	public void findTypePage(Page<DictionaryEntity> page);

	public void findDataPage(Page<DictionaryEntity> page, DictionaryEntity dict);

	public DictionaryEntity saveData(DictionaryEntity data);

	public DictionaryEntity saveType(DictionaryEntity type);

	public BaseEntityManager getBaseEntityManager();

}