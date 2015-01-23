package org.onetwo.plugins.admin.model.data.service;

import java.util.List;

import org.onetwo.common.utils.Page;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;

public interface DictionaryService {
	/****
	 * 如果类型和数据之间可以通过前缀识别的，可以通过类型的code作为前缀来查询所有类型下的字典数据
	 * @param code
	 * @return
	 */
	public List<DictionaryEntity> findDataByPrefixCode(String code, Object... properties);
	/***
	 * 根据代码查找字典或类型
	 * @param code
	 * @return
	 */
	public DictionaryEntity findByCode(String code);
	public DictionaryEntity getData(String value, String typeCode);
	public List<DictionaryEntity> findDataByTypeCode(String code);
	
	
	
	public int importDatas();
	public DictionaryEntity findById(Long id);

	public DictionaryEntity removeById(Long id);

	public void findTypePage(Page<DictionaryEntity> page);

	public void findDataPage(Page<DictionaryEntity> page, DictionaryEntity dict);

	public DictionaryEntity saveData(DictionaryEntity data);

	public DictionaryEntity saveType(DictionaryEntity type);

//	public BaseEntityManager getBaseEntityManager();

}