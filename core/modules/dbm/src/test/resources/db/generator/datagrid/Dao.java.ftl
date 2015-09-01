<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign daoPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.dao"/>
<#assign daoClassName="${_tableContext.className}Dao"/>
<#assign daoPropertyName="${_tableContext.propertyName}Dao"/>
<#assign entityClassName="${_tableContext.className}ExtEntity"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${daoPackage};

import java.util.List;

import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${entityClassName};
import ${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity.${_tableContext.className}Example;


public interface ${daoClassName} {
    
    public List<${entityClassName}> findPage(${_tableContext.className}Example example);
    
    public ${entityClassName} findById(${idType} ${idName});
    
}