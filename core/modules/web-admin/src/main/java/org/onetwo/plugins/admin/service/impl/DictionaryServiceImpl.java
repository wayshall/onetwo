package org.onetwo.plugins.admin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.entity.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DictionaryServiceImpl {
	
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Transactional(readOnly=true)
	public List<DataDictionary> loadAll(){
		List<DataDictionary> dictList = Querys.from(baseEntityManager, DataDictionary.class)
												.where()
													.field("valid").equalTo(true)
												.end()
												.asc("sort")
												.toQuery()
												.list();
		return dictList;
	}
	
	public Page<DataDictionary> findPage(Page<DataDictionary> page, String parentCode){
		/*PageHelper.startPage(page.getPage(), page.getPageSize());	
		DataDictionaryExample example = new DataDictionaryExample();
		Criteria crieria = example.createCriteria();//.andIsValidEqualTo(true);
		if(StringUtils.isNotBlank(parentCode)){
			crieria.andParentCodeEqualTo(parentCode);
		}else{
			crieria.andParentCodeEqualTo("");
		}
		example.setOrderByClause("sort asc");
		Page<DataDictionary> rows = (Page<DataDictionary>)dataDictionaryMapper.selectByExample(example);
		page.setTotal(rows.getTotal());*/
		
		return Querys.from(baseEntityManager, DataDictionary.class)
						.where()
							.field("parentCode").equalTo(parentCode==null?"":parentCode)
							.field("valid").equalTo(true)
						.end()
						.asc("sort")
						.toQuery()
						.page(page);
	}

	public List<DataDictionary> findChildren(String parentCode){
		/*DataDictionaryExample example = new DataDictionaryExample();
		Criteria c = example.createCriteria();//.andIsValidEqualTo(true)
		if(StringUtils.isNotBlank(parentCode)){
			c.andParentCodeEqualTo(parentCode.toUpperCase());
		}
		example.setOrderByClause("sort asc");
		List<DataDictionary> rows = dataDictionaryMapper.selectByExample(example);
		return rows;*/
		return Querys.from(baseEntityManager, DataDictionary.class)
						.where()
						.field("parentCode").equalTo(parentCode)
						.field("valid").equalTo(true)
						.end()
						.asc("sort")
						.toQuery()
						.list();
	}
	
	public void save(DataDictionary dictionary){
		this.checkDataDictionary(dictionary);
		Date now = new Date();
		dictionary.setCreateAt(now);
		dictionary.setUpdateAt(now);
//		return dataDictionaryMapper.insert(dictionary);
		dictionary.setCode(dictionary.getCode().toUpperCase());
		baseEntityManager.persist(dictionary);
	}
	
	private void checkDataDictionary(DataDictionary dictionary){
		/*if(StringUtils.isNotBlank(dictionary.getParentCode())){
			if(!dictionary.getCode().startsWith(dictionary.getParentCode())){
				throw new ServiceException("字典编码必须以所属类别编码为前缀：" + dictionary.getCode());
			}
		}*/
		if(StringUtils.isNotBlank(dictionary.getParentCode())){
			dictionary.setCode(dictionary.getParentCode()+"_"+dictionary.getValue());
		}else{
			dictionary.setCode(dictionary.getValue());
		}
	}
	
	public DataDictionary findByCode(String code){
		return baseEntityManager.findById(DataDictionary.class, code);
	}
	
	public void update(DataDictionary dictionary){
		this.checkDataDictionary(dictionary);
		dictionary.setUpdateAt(new Date());
//		return dataDictionaryMapper.updateByPrimaryKey(dictionary);
		baseEntityManager.update(dictionary);
	}
	
	public void deleteByCodes(String... codes){
		if(ArrayUtils.isEmpty(codes))
			throw new ServiceException("请先选择数据！");
		Stream.of(codes).forEach(code->deleteByCode(code));
	}
	
	public void deleteByCode(String code){
		DataDictionary dict = findByCode(code);
		if(dict==null){
			throw new ServiceException("找不到数据:" + code);
		}
		/*DataDictionaryExample example = new DataDictionaryExample();
		example.createCriteria().andParentCodeEqualTo(code);
		int childCount = dataDictionaryMapper.countByExample(example);*/
		int childCount = baseEntityManager.countRecord(DataDictionary.class, "parentCode", code).intValue();
		if(childCount>0){
			throw new ServiceException("该数据有子节点，无法删除：" + code);
		}
//		return dataDictionaryMapper.deleteByPrimaryKey(code);
		baseEntityManager.removeById(DataDictionary.class, code);
	}

	@Transactional(readOnly=true)
	public DataDictionary getByTypeAndValue(String parentCode, String value){
		return Querys.from(baseEntityManager, DataDictionary.class)
					  .where()
					  .field("parentCode").equalTo(parentCode)
					  .field("value").equalTo(value).end()
					  .toQuery().one();
	}
}
