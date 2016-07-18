package org.onetwo.plugins.admin.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.spring.underline.CopyUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.entity.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;

@Service
@Transactional
@Slf4j
public class DictionaryCachingServiceImpl {

	@Autowired
	private DictionaryServiceImpl dictionaryServiceImpl;
	
	private LoadingCache<String, DataDictionary> codeCaches 
									= CacheBuilder.newBuilder()
										.refreshAfterWrite(10, TimeUnit.MINUTES)
										.build(new CacheLoader<String, DataDictionary>(){

											@Override
											public DataDictionary load(String code) throws Exception {
												return dictionaryServiceImpl.findByCode(code);
											}
											
										});
	
	private LoadingCache<String, List<DataDictionary>> typeCaches 
									= CacheBuilder.newBuilder()
										.refreshAfterWrite(10, TimeUnit.MINUTES)
										.build(new CacheLoader<String, List<DataDictionary>>(){

											@Override
											public List<DataDictionary> load(String typeCode) throws Exception {
												List<DataDictionary> list = dictionaryServiceImpl.findChildren(typeCode);
												return ImmutableList.copyOf(list);
											}
											
										});

	@PostConstruct
	public void initCaches(){
		List<DataDictionary> dictList = this.dictionaryServiceImpl.loadAll();
		dictList.stream().forEach(dict->{
			this.codeCaches.put(dict.getCode(), dict);
		});
		Map<String, List<DataDictionary>> dictGroups = dictList.stream().collect(Collectors.groupingBy(d->d.getParentCode()));
		dictGroups.entrySet().forEach(g->{
			this.typeCaches.put(g.getKey(), g.getValue());
		});
	}
	public Optional<DataDictionary> findByCode(String code){
		try {
			DataDictionary data = codeCaches.get(code);
			return Optional.of(CopyUtils.copy(DataDictionary.class, data));
		} catch (Exception e) {
			log.error("find DataDictionary cache error: " + code, e);
			return Optional.ofNullable(null);
//			throw new ServiceException("find cache error: " + code, e);
		}
	}

	public Optional<DataDictionary> findByValue(String typeCode, String value){
		if(StringUtils.isBlank(typeCode)){
			return findByCode(value);
		}
		Collection<DataDictionary> list = findByType(typeCode);
		Optional<DataDictionary> data = list.stream()
							.filter(dict->dict.getValue().equalsIgnoreCase(value))
							.map(d->CopyUtils.copy(DataDictionary.class, d))
							.findAny();
		return data;
	}
	
	public List<DataDictionary> findByType(String typeCode){
		if(StringUtils.isBlank(typeCode))
			return Collections.emptyList();
		try {
			List<DataDictionary> list = typeCaches.get(typeCode);
			if(LangUtils.isEmpty(list))
				return list;
			return CopyUtils.copy(DataDictionary.class, list);
		} catch (ExecutionException e) {
			log.error("find DataDictionary cache error, type: " + typeCode, e);
			return Collections.emptyList(); 
		}
	}


}
