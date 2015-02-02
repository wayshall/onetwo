package org.onetwo.plugins.admin.model.data.service.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.context.ApplicationConfigKeys;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.plugins.admin.annotation.Dictionary;
import org.onetwo.plugins.admin.model.data.DictionaryEnum;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity.DicType;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;
import org.onetwo.plugins.admin.model.vo.DictTypeVo;
import org.onetwo.plugins.admin.utils.AdminDataUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DictionaryServiceImpl implements DictionaryService  {
	
	@Resource
	private BaseEntityManager baseEntityManager;

	@Value(ApplicationConfigKeys.BASE_PACKAGE_EXPR)
	protected String jfishBasePackages;
	
	private final ResourcesScanner classScaner = ResourcesScanner.CLASS_CANNER;
	
	@Override
	public int importDatas() {
		int totalCount = 0;
		
		//xml
		org.springframework.core.io.Resource dictResource = SpringUtils.newClassPathResource("/data/dict.xml");
		List<DictTypeVo> datas = AdminDataUtils.readDictResource(dictResource);
		totalCount = importDatas(datas);
		
		//enum
		datas = scanEnums();
		totalCount += importDatas(datas);
		
		return totalCount;
	}
	
	private List<DictTypeVo> scanEnums(){
		List<DictTypeVo> dtlist = this.classScaner.scan(new ScanResourcesCallback<DictTypeVo>(){

			@Override
			public DictTypeVo doWithCandidate(MetadataReader metadataReader, org.springframework.core.io.Resource resource, int count) {
				if (!metadataReader.getAnnotationMetadata().hasAnnotation(Dictionary.class.getName()))
					return null;
				Class<?> enumClass = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
				if (!enumClass.isEnum())
					return null;
				Dictionary ditAnno = enumClass.getAnnotation(Dictionary.class);
				DictTypeVo dt = new DictTypeVo();
				dt.setEnumValue(true);
				dt.setCode(enumClass.getSimpleName());
				dt.setName(StringUtils.firstNotBlank(ditAnno.name(), dt.getCode()));
				Enum<?>[] values = (Enum<?>[]) enumClass.getEnumConstants();
				
				List<DictionaryEntity> dictList = LangUtils.newArrayList(values.length);
				for(Enum<?> e : values){
					DictionaryEntity dict = new DictionaryEntity();
					dict.setCode(dt.getCode()+"_"+e.name());
					dict.setValid(true);
					dict.setEnumValue(true);
					
					if(DictionaryEnum.class.isInstance(e)){
						DictionaryEnum de = (DictionaryEnum) e;
						dict.setName(de.getName());
						dict.setValue(de.getValue());
						dict.setSort(de.getSort());
						dict.setRemark(de.getRemark());
						
					}else{
						BeanWrapper bw = SpringUtils.newBeanWrapper(e);
						if(bw.isReadableProperty("value")){
							dict.setValue(StringUtils.emptyIfNull(bw.getPropertyValue("value")));
						}else{
							dict.setValue(e.name());
						}
						if(bw.isReadableProperty("name")){
							dict.setName(StringUtils.emptyIfNull(bw.getPropertyValue("name")));
						}else if(bw.isReadableProperty("label")){
							dict.setName(StringUtils.emptyIfNull(bw.getPropertyValue("label")));
						}
						if(bw.isReadableProperty("sort")){
							dict.setSort(Types.convertValue(bw.getPropertyValue("sort"), int.class));
						}
						if(bw.isReadableProperty("remark")){
							dict.setRemark(StringUtils.emptyIfNull(bw.getPropertyValue("remark")));
						}
					}
					dictList.add(dict);
					
				}
				
				dt.setDicts(dictList);
				return dt;
			}
			
			
		}, jfishBasePackages);
		
		return dtlist;
	}

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
			if(dictType.getEnumValue()==null){
				dictType.setEnumValue(false);
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
				if(dictType.getEnumValue()==null){
					dictType.setEnumValue(false);
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
	
	public <T> T findValueByCode(String code, Class<T> valueType, T defValue){
		DictionaryEntity dict = findByCode(code);
		if(dict==null){
			return defValue;
		}
		return Types.convertValue(dict.getValue(), valueType, defValue);
	}
	
	public List<DictionaryEntity> findDataByPrefixCode(String code, Object... properties){
		Map<Object, Object> params = CUtils.asMap(properties);
		params.put("code:like", code+"_");
		params.put("dictType", DicType.DATA);
		params.put(K.ASC, "sort");
		params.put(K.IF_NULL, IfNull.Ignore);
		return getBaseEntityManager().findByProperties(DictionaryEntity.class,  params);
	}
	
	public List<DictionaryEntity> findDataByTypeCode(String code){
		DictionaryEntity type = getBaseEntityManager().findOne(DictionaryEntity.class, "code", code, "dictType", DicType.DATA);
		if(type==null)
			throw new BaseException("找不到字典类型：" + code);
		return getBaseEntityManager().findByProperties(DictionaryEntity.class, "typeId", type.getId(), "dictType", DicType.DATA, K.ASC, "sort");
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

	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	@Override
	public DictionaryEntity getData(String value, String typeCode) {
		if(StringUtils.isBlank(typeCode)){
			return baseEntityManager.findUnique(DictionaryEntity.class, "dictType", DicType.DATA, "value", value);
		}else{

			DictionaryEntity type = findByCode(typeCode);
			if(type==null)
				throw new BaseException("找不到字典类型：" + typeCode);
			return baseEntityManager.findUnique(DictionaryEntity.class, "typeId", type.getId(), "dictType", DicType.DATA, "value", value);
		}
	}

}