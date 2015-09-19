<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign servicePackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.service"/>
<#assign serviceImplClassName="${_tableContext.className}ServiceImpl"/>
<#assign serviceImplPropertyName="${_tableContext.propertyName}ServiceImpl"/>
<#assign mapperClassName="${_tableContext.className}Mapper"/>
<#assign mapperPropertyName="${_tableContext.propertyName}Mapper"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${servicePackage};

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.boot.mybatis.MyBatisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.onetwo.easyui.EasyPage;
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.mapper.${mapperClassName};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example;
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example.Criteria;

@Service
@Transactional
public class ${serviceImplClassName} {

    @Autowired
    private ${mapperClassName} ${mapperPropertyName};

    
    public void findPage(EasyPage<${_tableContext.className}> page, ${_tableContext.className} ${_tableContext.propertyName}){
        MyBatisUtils.setCurrentQueryPage(page);
        ${_tableContext.className}Example example = new ${_tableContext.className}Example();
        Criteria crieria = example.createCriteria();
        
    <#list table.columns as column>
      <#if !column.primaryKey>
        <#if column.mapping.isStringType()==true>
        Optional.ofNullable(${_tableContext.propertyName}.${column.getReadMethodName(false)}())
                .filter(field->StringUtils.isNotBlank(field))
                .ifPresent(field->crieria.and${column.javaName?cap_first}Like(StringUtils.getSqlLikeString(field)));
        <#else>  
        Optional.ofNullable(${_tableContext.propertyName}.${column.getReadMethodName(false)}())
                .ifPresent(field->crieria.and${column.javaName?cap_first}EqualTo(field));
        </#if>
      </#if>
    </#list>

        List<${_tableContext.className}> rows = ${mapperPropertyName}.selectByExample(example);
        page.setRows(rows);
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
        Assert.notNull(${_tableContext.propertyName}.get${idName?cap_first}(), "参数不能为null");
        ${_tableContext.className} db${_tableContext.className} = ${mapperPropertyName}.selectByPrimaryKey(${_tableContext.propertyName}.get${idName?cap_first}());
        if(db${_tableContext.className}==null){
            throw new ServiceException("找不到数据：" + ${_tableContext.propertyName}.get${idName?cap_first}());
        }
        ReflectUtils.copyIgnoreBlank(${_tableContext.propertyName}, db${_tableContext.className});
        db${_tableContext.className}.setUpdateAt(new Date());
        return ${mapperPropertyName}.updateByPrimaryKey(db${_tableContext.className});
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
