package org.onetwo.plugins.admin.model.data.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity.DicType;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;
import org.onetwo.plugins.admin.model.vo.DictTypeVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DictionaryServiceImpl implements DictionaryService  {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	public int importDatas(List<DictTypeVo> datas){
		int totalCount = 0;
		if(LangUtils.isEmpty(datas))
			return totalCount;
		int typeCount = 1;
		for(DictTypeVo dictType : datas){
			if(dictType.getSort()==null){
				dictType.setSort(typeCount);
			}
			if(dictType.getValid()==null){
				dictType.setValid(true);
			}
			DictionaryEntity type = findByCode(dictType.getCode());
			if(type==null){
				DictionaryEntity newTyep = new DictionaryEntity();
				ReflectUtils.copyIgnoreBlank(dictType, newTyep);
				type = saveType(newTyep);
			}else{
				HibernateUtils.copyIgnoreRelationsAndFields(dictType, type);
				type = saveType(type);
			}
			typeCount++;
			totalCount++;
			
			int dictCount = 1;
			for(DictionaryEntity dict : dictType.getDicts()){
				if(dict.getSort()<1){
					dict.setSort(dictCount);
				}
				if(dict.getValid()==null){
					dict.setValid(true);
				}
				DictionaryEntity dictData = findByCode(dict.getCode());
				if(dictData==null){
					DictionaryEntity newDictData = new DictionaryEntity();
					ReflectUtils.copyIgnoreBlank(dict, newDictData);
					newDictData.setTypeId(type.getId());
					saveData(newDictData);
				}else{
					HibernateUtils.copyIgnoreRelationsAndFields(dict, dictData);
					dictData.setTypeId(type.getId());
					saveData(dictData);
				}
				dictCount++;
				totalCount++;
			}
		}
		return totalCount;
	}
	
	public DictionaryEntity findByCode(String code){
		return getBaseEntityManager().findOne(DictionaryEntity.class, "code", code);
	}
	
	public List<DictionaryEntity> findByPrefixCode(String code){
		return getBaseEntityManager().findByProperties(DictionaryEntity.class, "code:like", code+"_", K.ASC, "sort");
	}

	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#findById(java.lang.Long)
	 */
	@Override
	public DictionaryEntity findById(Long id){
		return baseEntityManager.findById(DictionaryEntity.class, id);
	}
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#removeById(java.lang.Long)
	 */
	@Override
	public DictionaryEntity removeById(Long id){
		DictionaryEntity dict = baseEntityManager.removeById(DictionaryEntity.class, id);
		/*if(dict.getDictType()==DicType.TYPE){
			if(getBaseEntityManager().countRecord(DictionaryEntity.class, "typeId", dict.getId()).intValue()>0){
				throw new ServiceException("字典类型下数据不为空，不能删除！");
			}
		}*/
		return dict;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#findTypePage(org.onetwo.common.utils.Page)
	 */
	@Override
	public void findTypePage(Page<DictionaryEntity> page){
		getBaseEntityManager().findPage(DictionaryEntity.class, page, "dictType", DicType.TYPE, K.DESC, "id");
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#findDataPage(org.onetwo.common.utils.Page, org.onetwo.plugins.admin.model.data.entity.DictionaryEntity)
	 */
	@Override
	public void findDataPage(Page<DictionaryEntity> page, DictionaryEntity dict){
		getBaseEntityManager().findPage(DictionaryEntity.class, page, "typeId", dict.getTypeId(), "dictType", DicType.DATA, K.DESC, "id");
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#saveData(org.onetwo.plugins.admin.model.data.entity.DictionaryEntity)
	 */
	@Override
	public DictionaryEntity saveData(DictionaryEntity data){
		data.setCode(data.getCode().toUpperCase());
		data.setDictType(DicType.DATA);
		DictionaryEntity type = getBaseEntityManager().load(DictionaryEntity.class, data.getTypeId());
//		data.setCode(type.getCode()+"_"+data.getCode().toUpperCase());
		if(!data.getCode().startsWith(type.getCode()+"_")){
			throw new ServiceException("字典数据编码必须以类型编码+下划线为前缀，类型编码:"+type.getCode()+", 输入编码"+data.getCode());
		}
		if(StringUtils.isBlank(data.getValue())){
			data.setValue(data.getCode());
		}
		baseEntityManager.save(data);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#saveType(org.onetwo.plugins.admin.model.data.entity.DictionaryEntity)
	 */
	@Override
	public DictionaryEntity saveType(DictionaryEntity type){
		type.setCode(type.getCode().toUpperCase());
		type.setDictType(DicType.TYPE);
		if(StringUtils.isBlank(type.getValue())){
			type.setValue(type.getCode());
		}
		baseEntityManager.save(type);
		return type;
	}

	/* (non-Javadoc)
	 * @see org.onetwo.plugins.admin.model.data.service.DictionaryService#getBaseEntityManager()
	 */
	@Override
	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

}