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
	
	<widget:grid2 name="usergrid" dataSource="${'$'}{page}" title="用户列表" toolbar="true">
		<widget:row2 name="entity" type="iterator" renderHeader="true">
			<widget:field2 name="ids" render="checkbox" value="id"/>
			<widget:field2 name="id" label="主键"/>
			<widget:field2 name="userName" label="用户名"/>
			<widget:field2 name="email" label="电子邮件"/>
			<widget:field2 name="createTime" label="创建时间" dataFormat="yyyy-MM-dd"/>
			<widget:field2 name="operation" label="操作" render="html">
				<a href="${'$'}{siteConfig.baseURL}${webPath}/${'$'}{entity.${table.primaryKey.javaName}}/edit">编辑</a>
			</widget:field2>
		</widget:row2>
	</widget:grid2>
	
  </layout:override>
  
</layout:extends>

