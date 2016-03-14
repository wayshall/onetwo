<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.entity"/>
<#assign entityClassName="${_tableContext.className}ExtEntity"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${entityPackage};

public class ${entityClassName} extends ${_tableContext.className} {

}
