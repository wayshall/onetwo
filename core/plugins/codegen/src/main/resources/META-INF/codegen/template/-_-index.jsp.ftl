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
	
	<widget:dataGrid name="usergrid" dataSource="${'$'}{page}" title="用户列表" toolbar="true">
		<widget:dataRow name="entity" type="iterator" renderHeader="true">
			<widget:dataField name="ids" label="全选" render="checkbox" value="id"/>
			<widget:dataField name="id" label="主键"/>
			<widget:dataField name="userName" label="用户名"/>
			<widget:dataField name="email" label="电子邮件"/>
			<widget:dataField name="createTime" label="创建时间" dataFormat="yyyy-MM-dd"/>
			<widget:dataField name="operation" label="操作" render="html">
				<a href="${'$'}{siteConfig.baseURL}${webPath}/${'$'}{entity.${table.primaryKey.javaName}}/edit">编辑</a>
			</widget:dataField>
		</widget:dataRow>
	</widget:dataGrid>
	
  </layout:override>
  
</layout:extends>

