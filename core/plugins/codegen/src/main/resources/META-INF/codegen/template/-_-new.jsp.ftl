<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<layout:extends>	

	
	<layout:override name="title">
		新建 ${commonName}
	</layout:override>
	
 	<layout:override name="main-content">
 
	<form:form modelAttribute="${commonName_uncapitalize}" action="${"$"}{siteConfig.baseURL}${webPath}" method="post">
		
<div class="page-header">
<h2>
新建 ${commonName}
</h2>
</div>
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
			<input name="" type="submit" value="保存" class="btn btn-primary"/>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		</table>
	</form:form>
	
	
  </layout:override>
  
</layout:extends>