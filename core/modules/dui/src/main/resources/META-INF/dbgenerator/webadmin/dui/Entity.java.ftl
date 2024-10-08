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

import org.onetwo.dbm.annotation.SnowflakeId;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.dbm.ui.annotation.DUIEntity;
import org.onetwo.dbm.ui.annotation.DUIField;
import org.onetwo.dbm.ui.annotation.DUIInput;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
@DUIEntity(
        name = "${_tableContext.className}", 
        label = "${(table.comments[0])!''}"
)
public class ${entityClassName} extends <#if baseEntityClass??>${baseEntityClass}<#else>BaseEntity</#if> {

    @SnowflakeId
    ${table.primaryKey.javaType.simpleName} ${table.primaryKey.propertyName};
    
<#list table.columns as column>
<#if column.primaryKey == false && 
    !( column.propertyName == 'createAt' || 
        column.propertyName == 'updateAt' || 
        column.propertyName == 'createBy' ||
        column.propertyName == 'updateBy')>
    /***
     * ${(column.comments[0])!''}
     */
    <#if column.isJsonType()>
    @org.onetwo.dbm.annotation.DbmJsonField
    <#elseif column.mapping.isStringType()>
    @Length(max=${column.columnSize})
    <#elseif column.isEmailType()>
    @Email
    <#elseif column.isUrlType()>
    @URL
    <#elseif column.isFileType()>
    @JsonSerialize(using=ImageUrlJsonSerializer.class)
    </#if>
    <#if column.isFileType()==true>
    @DUIInput(type=DUIInput.InputTypes.FILE)
    <#elseif column.mapping.isSqlTimestamp()==true>
    @DUIInput(type=DUIInput.InputTypes.DATE_TIME)
    <#elseif column.mapping.isSqlTime()==true>
    @DUIInput(type=DUIInput.InputTypes.TIME)
    <#elseif column.mapping.isSqlDate()==true>
    @DUIInput(type=DUIInput.InputTypes.DATE)
    <#elseif column.mapping.isBooleanType()==true>
    @DUIInput(type=DUIInput.InputTypes.SWITCH)
    <#elseif column.mapping.isNumberType()==true>
    @DUIInput(type=DUIInput.InputTypes.NUMBER)
    <#else>
        <#if column.columnSize gt 255>
    @DUIInput(type=DUIInput.InputTypes.TEXTAREA)
        </#if>
    </#if>
    @DUIField(label = "${(column.comments[0])!''}", order = ${column?counter}0)
    ${column.mappingJavaClassLabel} ${column.propertyName};
    
</#if>
</#list>
}
