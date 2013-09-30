<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<#list table.columnCollection as column>
	<#if !column.primaryKey>
	<widget:formField name="${column.javaName}" label="${column.javaName}"/>
	</#if>
</#list>
	<widget:formField name="" type="submit" label="提交"/>
	<widget:formField name="" type="button" value="${"$"}{siteConfig.baseURL}${webPath}" label="返回"/>

