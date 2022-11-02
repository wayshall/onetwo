<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityClassName="${_tableContext.className}PageRequest"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>
<#assign searchableFields=DUIEntityMeta.searchableFields/>

package ${_tableContext.localFullPackage};

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import org.onetwo.common.utils.PageRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * ${(table.comments[0])!''}
 */
// @SuppressWarnings("serial")
@Data
public class ${entityClassName} extends PageRequest {

    // ${table.primaryKey.javaType.simpleName} ${table.primaryKey.propertyName};
    
<#if searchableFields.isEmpty()==false>
    <#list searchableFields as column>
        <#if column.primaryKey == false && !( column.propertyName == 'createAt' || column.propertyName == 'createBy' || column.propertyName == 'updateAt' || column.propertyName == 'updateBy' || column.propertyName == 'tenantId' ) >
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
    // @SafeHtml
    <#elseif column.isEmailType()>
    @Email
    <#elseif column.isUrlType()>
    @URL
    </#if>
    @ApiModelProperty("${(column.comments[0])!''}")
    ${column.mappingJavaClassLabel} ${column.propertyName};
        </#if>
    </#list>
</#if>
}
