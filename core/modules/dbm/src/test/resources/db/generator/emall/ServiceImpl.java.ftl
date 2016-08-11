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
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.mapper.${mapperClassName};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example;
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example.Criteria;

import com.yooyo.emall.common.ErrorCode;
import com.yooyo.emall.common.Page;
import com.yooyo.emall.common.ServiceException;
import com.yooyo.emall.common.utils.CommonUtils;

@Service
@Transactional
public class ${serviceImplClassName} {

    @Autowired
    private ${mapperClassName} ${mapperPropertyName};

    @Transactional(readOnly=true)
    public Page<${_tableContext.className}> findPage(Page<${_tableContext.className}> page, ${_tableContext.className} ${_tableContext.propertyName}){
       ${_tableContext.className}Example example = new ${_tableContext.className}Example();
        
        int total = ${mapperPropertyName}.countByExample(example);
        page.setTotalCount(total);
        if(total>0){
            List<${_tableContext.className}> rows = ${mapperPropertyName}.selectByExampleWithRowbounds(example, CommonUtils.createRowBounds(page));
            page.setResult(rows);
        }
        return page;
    }
    
    public void save(${_tableContext.className} ${_tableContext.propertyName}){
        Date now = new Date();
        <#if table.hasColumn('Last_Update_Time')>
        ${_tableContext.propertyName}.setLastUpdateTime(now);
        </#if>
        <#if table.hasColumn('Create_Time')>
        ${_tableContext.propertyName}.setCreateTime(now);
        </#if>
        ${mapperPropertyName}.insert(${_tableContext.propertyName});
    }
    
    public ${_tableContext.className} findById(${idType} ${idName}){
        return ${mapperPropertyName}.selectByPrimaryKey(id);
    }
    
    public void update(${_tableContext.className} ${_tableContext.propertyName}){
        if(${_tableContext.propertyName}.${table.primaryKey.readMethodName}()==null){
            throw new ServiceException(ErrorCode.COM_ERR_VALIDATION_REQUIRED, "id不能为null");
        }
        <#if table.hasColumn('Last_Update_Time')>
        ${_tableContext.propertyName}.setLastUpdateTime(new Date());
        </#if>
        int updateCount = ${mapperPropertyName}.updateByPrimaryKeySelective(${_tableContext.propertyName});
        if(updateCount<1){
            throw new ServiceException("更新失败，没有找到数据：%s ", ${_tableContext.propertyName}.getId());
        }
    }
    
    public void deleteByIds(${idType}...${idName}s){
        Stream.of(${idName}s).forEach(${idName}->deleteById(${idName}));
    }
    
    
    <#if table.hasColumn('state')==true>
    public void deleteById(${idType} ${idName}){
        if(id==null){
            throw new ServiceException(ErrorCode.COM_ERR_VALIDATION_REQUIRED, "${idName}不能为null");
        }
        ${_tableContext.className} ${_tableContext.propertyName} = new ${_tableContext.className}();
        ${_tableContext.propertyName}.set${idName?cap_first}(${idName});
        ${_tableContext.propertyName}.setState(${_tableContext.className}State.DELETE.getValue());
        int deleteCount = ${mapperPropertyName}.updateByPrimaryKey(${_tableContext.propertyName});
        if(deleteCount<1){
            throw new ServiceException("删除失败，没有找到数据！(%s)", id);
        }
    }
    <#else>
    public void deleteById(${idType} ${idName}){
        if(id==null){
            throw new ServiceException(ErrorCode.COM_ERR_VALIDATION_REQUIRED, "${idName}不能为null");
        }
        int deleteCount = ${mapperPropertyName}.deleteByPrimaryKey(${idName});
        if(deleteCount<1){
            throw new ServiceException("删除失败，没有找到数据：%s ", ${idName});
        }
    }
    </#if>
    
}