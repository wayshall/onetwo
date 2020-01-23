<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityClassName="${_tableContext.className}Entity"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${_tableContext.localFullPackage};

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import org.onetwo.dbm.annotation.SnowflakeId;
import org.onetwo.dbm.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * ${(table.comments[0])!''}
 */
@SuppressWarnings("serial")
@Entity
@Table(name="${table.name}")
@Data
@EqualsAndHashCode(callSuper=true)
public class ${entityClassName} extends <#if baseEntityClass??>${baseEntityClass}<#else>BaseEntity</#if> {

    @SnowflakeId
    @NotNull
    ${table.primaryKey.javaType.simpleName} ${table.primaryKey.propertyName};
    
<#list table.columns as column>
<#if column.primaryKey == false && !( column.propertyName == 'createAt' || column.propertyName == 'updateAt' ) >
    /***
     * ${(column.comments[0])!''}
     */
    <#if !column.nullable>
    @NotNull
    </#if>
    <#if column.isJsonType()>
    @org.onetwo.dbm.annotation.DbmJsonField
    <#elseif column.mapping.isStringType()>
    @Length(max=${column.columnSize})
    @SafeHtml
    <#elseif column.isEmailType()>
    @Email
    <#elseif column.isUrlType()>
    @URL
    </#if>
    ${column.mappingJavaClassLabel} ${column.propertyName};
    
</#if>
</#list>
}
