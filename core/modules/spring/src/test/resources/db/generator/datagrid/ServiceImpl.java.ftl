<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign servicePackage="com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.service"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${servicePackage};

import java.util.Date;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.onetwo.easyui.EasyPage;
import com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.entity.${_tableContext.className};
import com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.mapper.${mapperClassName};
import com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example;
import com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example.Criteria;

@Service
@Transactional
public class ${serviceImplClassName} {

    @Autowired
    private ${mapperClassName} ${mapperPropertyName};

    
    public Page<${_tableContext.className}> findPage(EasyPage<${_tableContext.className}> page){
        PageHelper.startPage(page.getPage(), page.getPageSize());   
        ${_tableContext.className}Example example = new ${_tableContext.className}Example();
        Criteria crieria = example.createCriteria();
        Page<${_tableContext.className}> rows = (Page<${_tableContext.className}>)${mapperPropertyName}.selectByExample(example);
        page.setTotal(rows.getTotal());
        return rows;
    }
    
    public int save(${_tableContext.className} ${_tableContext.propertyName}){
        Date now = new Date();
        ${_tableContext.propertyName}.setCreateAt(now);
        ${_tableContext.propertyName}.setUpdateAt(now);
        return ${mapperPropertyName}.insert(${_tableContext.propertyName});
    }
    
    public ${_tableContext.className} findByPrimaryKey(${idType} ${idName}){
        return ${mapperPropertyName}.selectByPrimaryKey(${idName});
    }
    
    public int update(${_tableContext.className} ${_tableContext.propertyName}){
        ${_tableContext.propertyName}.setUpdateAt(new Date());
        return ${mapperPropertyName}.updateByPrimaryKey(${_tableContext.propertyName});
    }
    
    public void deleteByPrimaryKeys(${idType}...${idName}s){
        if(ArrayUtils.isEmpty(${idName}s))
            throw new ServiceException("请先选择数据！");
        Stream.of(${idName}s).forEach(${idName}->deleteByPrimaryKey(${idName}));
    }
    
    public int deleteByPrimaryKey(${idType} ${idName}){
        ${_tableContext.className} ${_tableContext.propertyName} = findByPrimaryKey(${idName});
        if(${_tableContext.propertyName}==null){
            throw new ServiceException("找不到数据:" + ${idName});
        }
        return ${mapperPropertyName}.deleteByPrimaryKey(${idName});
    }
}
