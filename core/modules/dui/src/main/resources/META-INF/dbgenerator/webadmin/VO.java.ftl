<#assign requestPath="/${_globalConfig.getModuleName()}/${_tableContext.className}"/>
<#assign pagePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

<#assign entityClassName="${_tableContext.className}VO"/>
<#assign entityClassName2="${_tableContext.className}"/>
<#assign idName="${table.primaryKey.javaName}"/>
<#assign idType="${table.primaryKey.javaType.simpleName}"/>

package ${_tableContext.localFullPackage};

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
// import org.hibernate.validator.constraints.SafeHtml;

import org.onetwo.dbm.annotation.SnowflakeId;
import org.onetwo.dbm.jpa.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModelProperty;

/***
 * ${(table.comments[0])!''}
 */
@SuppressWarnings("serial")
@Data
public class ${entityClassName} implements Serializable {

    ${table.primaryKey.javaType.simpleName} ${table.primaryKey.propertyName};
    
<#list table.columns as column>
    <#if column.primaryKey == false && !( column.propertyName == 'createAt' || column.propertyName == 'createBy' || column.propertyName == 'updateAt' || column.propertyName == 'updateBy' || column.propertyName == 'tenantId' ) >
    /***
     * ${(column.comments[0])!''}
     */
    @ApiModelProperty("${(column.comments[0])!''}")
    ${column.mappingJavaClassLabel} ${column.propertyName};
    
    </#if>
</#list>
}
