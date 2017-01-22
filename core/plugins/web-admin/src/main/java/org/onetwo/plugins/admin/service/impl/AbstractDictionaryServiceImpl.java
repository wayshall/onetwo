package org.onetwo.plugins.admin.service.impl;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.convert.Types;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.annotation.Dictionary;
import org.onetwo.plugins.admin.service.DictionaryImportService;
import org.onetwo.plugins.admin.utils.DataUtils;
import org.onetwo.plugins.admin.utils.DictionaryEnum;
import org.onetwo.plugins.admin.vo.DictInfo;
import org.onetwo.plugins.admin.vo.DictTypeInfo;
import org.onetwo.plugins.admin.vo.DictionaryList;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.transaction.annotation.Transactional;

//@Service
@Transactional
abstract public class AbstractDictionaryServiceImpl implements DictionaryImportService {
	
	private final ResourcesScanner classScaner = ResourcesScanner.CLASS_CANNER;
	
//	@Override
	public int importDatas(String dataPath) {
		int totalCount = 0;
		
		//xml "/data/dict.xml"
		org.springframework.core.io.Resource dictResource = SpringUtils.newClassPathResource(dataPath);
		DictionaryList datas = DataUtils.readDictResource(dictResource);
		totalCount = importDatas(datas);
		
		//enum
		if(StringUtils.isNotBlank(datas.getScanEnumPackages())){
			String[] packages = StringUtils.split(datas.getScanEnumPackages(), ",");
			Collection<DictTypeInfo> dictList = scanEnums(packages);
			totalCount += importDatas(dictList);
		}
		
		return totalCount;
	}
	
	private Collection<DictTypeInfo> scanEnums(String... basePackages){
		Collection<DictTypeInfo> dtlist = this.classScaner.scan(new ScanResourcesCallback<DictTypeInfo>(){

			@Override
			public DictTypeInfo doWithCandidate(MetadataReader metadataReader, org.springframework.core.io.Resource resource, int count) {
				if (!metadataReader.getAnnotationMetadata().hasAnnotation(Dictionary.class.getName()))
					return null;
				Class<?> enumClass = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
				if (!enumClass.isEnum())
					return null;
				Dictionary ditAnno = enumClass.getAnnotation(Dictionary.class);
				DictTypeInfo dt = new DictTypeInfo();
				dt.setEnumValue(true);
				dt.setCode(enumClass.getSimpleName());
				dt.setName(StringUtils.firstNotBlank(ditAnno.name(), dt.getCode()));
				Enum<?>[] values = (Enum<?>[]) enumClass.getEnumConstants();
				
				List<DictInfo> dictList = LangUtils.newArrayList(values.length);
				for(Enum<?> e : values){
					DictInfo dict = newDictInstance();
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
			
			
		}, basePackages);
		
		return dtlist;
	}

	public int importDatas(Collection<DictTypeInfo> datas){
		int totalCount = 0;
		if(LangUtils.isEmpty(datas))
			return totalCount;
		int typeCount = 1;
		for(DictTypeInfo dictType : datas){
			if(dictType.getSort()==null){
				dictType.setSort(typeCount);
			}
			if(dictType.getValid()==null){
				dictType.setValid(true);
			}
			if(dictType.getEnumValue()==null){
				dictType.setEnumValue(false);
			}
			DictInfo type = findByCode(dictType.getCode());
			if(type==null){
				DictInfo newTyep = newDictInstance();
				ReflectUtils.copyIgnoreBlank(dictType, newTyep);
//				newTyep.setDictType(dictty);
				type = saveDictType(newTyep, true);
			}else{
				ReflectUtils.copyIgnoreBlank(dictType, type);
				type = saveDictType(type, false);
			}
			typeCount++;
			totalCount++;
			
			int dictCount = 1;
			for(DictInfo dict : dictType.getDicts()){
				if(dict.getSort()<1){
					dict.setSort(dictCount);
				}
				if(dict.getValid()==null){
					dict.setValid(true);
				}
				if(dictType.getEnumValue()==null){
					dictType.setEnumValue(false);
				}
				DictInfo dictData = findByCode(dict.getCode());
				if(dictData==null){
					DictInfo newDictData = newDictInstance();
					ReflectUtils.copyIgnoreBlank(dict, newDictData);
					saveDictData(newDictData, true);
				}else{
					ReflectUtils.copyIgnoreBlank(dict, dictData);
					saveDictData(dictData, false);
				}
				dictCount++;
				totalCount++;
			}
		}
		return totalCount;
	}
	
	protected DictInfo newDictInstance(){
		return new DictInfo();
	}
	
	abstract public DictInfo findByCode(String code);
	
	abstract public DictInfo saveDictType(DictInfo type, boolean insert);
	
	abstract public DictInfo saveDictData(DictInfo data, boolean insert);

}