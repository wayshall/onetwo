<#assign webPath="${moduleRequestPath}/${commonName?lower_case}"/>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<layout:extends>	

	
	<layout:override name="title">
		${commonName}列表
	</layout:override>
	
 <layout:override name="main-content">
	
	<layout:override name="grid_toolbar">
		<li>
			<a href="${'$'}{siteConfig.baseURL}${webPath}/new"> 新 建  </a>
			</li>
			<li>
			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${'$'}{siteConfig.baseURL}${webPath}"> 批量删除  </a>
		</li>
	</layout:override>
	
	<widget:dataGrid name="usergrid" dataSource="${'$'}{page}" title="${commonName}列表" toolbar="true">
		<widget:dataRow name="entity" type="iterator" renderHeader="true">
			<widget:dataField name="${table.primaryKey.javaName}s" label="全选" render="checkbox" value="${table.primaryKey.javaName}"/>
		<#list table.columnCollection as column>
			<widget:dataField name="${column.javaName}" label="${column.javaName}"/>
		</#list>
			<widget:dataField name="operation" label="操作" render="html">
				<a href="${'$'}{siteConfig.baseURL}${webPath}/${'$'}{entity.${table.primaryKey.javaName}}/edit">编辑</a>
			</widget:dataField>
		</widget:dataRow>
	</widget:dataGrid>
	
  </layout:override>
  
</layout:extends>

