<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<layout:extends>	
	
	<layout:override name="title">
		编辑 ${commonName} ${"$"}{${commonName?uncap_first}.id} 
	</layout:override>
	
 	<layout:override name="main-content">
		
<div class="page-header">
<h2>
${commonName} ${"$"}{${commonName?uncap_first}.id} 编辑
</h2>
</div>

	<form:form modelAttribute="${commonName_uncapitalize}" action="${"$"}{siteConfig.baseURL}${webPath}/${"$"}{${commonName?uncap_first}.${table.primaryKey.javaName}}" method="put">
	
		<input name="${table.primaryKey.javaName}" type="hidden" value="${"$"}{${commonName?uncap_first}.${table.primaryKey.javaName}}"/>
		
		<table class="table table-bordered table-striped">
		<#list table.columnCollection as column>
			<#if !column.primaryKey>
			<tr>
				<td>${column.javaName} </td>
				<td>
				<form:input path="${column.javaName}"/>
				<form:errors path="${column.javaName}" cssClass="error"/>
				</td>
			</tr>
			</#if>
		</#list>
		
		<tr>
			<td colspan="2">
			<input name="" type="submit" value="更新" class="btn btn-primary"/>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</form:form>
	
	
  </layout:override>
  
</layout:extends>