<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityPackage="${_globalConfig.getJavaBasePackage()}.${_globalConfig.getModuleName()}.vo"/>
<#assign entityClassName="${_tableContext.className}Entity"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${entityPackage};

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
@Data
public class ${entityClassName} implements Serializable  {

<#list table.columns as column>
    <#if column.primaryKey>
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    </#if>
    private ${column.javaType.simpleName} ${column.propertyName};
</#list>
}
