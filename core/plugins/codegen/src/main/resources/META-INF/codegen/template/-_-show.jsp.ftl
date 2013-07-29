<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<layout:extends>	
	
	<layout:override name="title">
		显示 ${commonName} ${"$"}{${commonName?uncap_first}.id} 
	</layout:override>
	
 	<layout:override name="main-content">
		
<div class="page-header">
<h2>
${commonName} ${"$"}{${commonName?uncap_first}.id} 
</h2>
</div>

		<table class="table table-bordered table-striped">
		<#list table.columnCollection as column>
			<tr>
				<td  style="width:160px">${column.javaName} </td>
				<td>
				<c:out value="${"$"}{${commonName?uncap_first+"."+column.javaName}}"/>
				</td>
			</tr>
		</#list>
		<tr>
			<td colspan="2">
			<a href="${"$"}{siteConfig.baseURL}${webPath}/${"$"}{${commonName?uncap_first}.${table.primaryKey.javaName}}/edit" class="btn btn-primary">编辑</a>
			<a href="${"$"}{siteConfig.baseURL}${webPath}" class="btn">返回</a>
			</td>
		</tr>
		
		</table>

  </layout:override>
  
</layout:extends>