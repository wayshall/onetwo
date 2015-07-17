<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign daoPackage="com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.dao"/>
<#assign daoClassName="${_tableContext.className}Dao"/>
<#assign daoPropertyName="${_tableContext.propertyName}Dao"/>
<#assign entityClassName="${_tableContext.className}ExtEntity"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${daoPackage};

import java.util.List;

import com.yooyo.zhiyetong.${_globalConfig.getModuleName()}.entity.${entityClassName};


public interface ${daoClassName} {
    
    public List<${entityClassName}> findPage();
    
}