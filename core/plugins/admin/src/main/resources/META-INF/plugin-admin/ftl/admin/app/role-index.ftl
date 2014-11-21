

<@extends>	

	
	<@override name="title">
		Role列表
	</@override>
	
 <@override name="main-content">


<ul class="nav nav-tabs" id="appTabs"> 	
	<#list apps as app>
		<#if app.code==role.appCode>
 			<li class="active"><a href="#appUserTab"  data-toggle="tab">${app.name }</a></li>
		<#else>
 			<li><a href="${pluginConfig.baseURL}/app/role?appCode=${app.code}"  >${app.name }</a></li>
		</#if>
	</#list>
</ul>

<div class="tab-content">
   <div id="appRoleTab" class="tab-pane active">	
	
	<@layout.override name="grid_toolbar">
	    <@security.hasPermission  code="plugin:AdminModule$AppRole$New">
			<li>
			<a href="${pluginConfig.baseURL}/app/role/new?appCode=${role.appCode}"> 新 建  </a>
			</li>
		</@security.hasPermission>
	    <@security.hasPermission  code="plugin:AdminModule$AppRole$Delete">
 			<li> 
 			<a data-method="delete" class="dg-toolbar-button-delete" data-confirm="确定要批量删除这些数据？" href="${pluginConfig.baseURL}/app/role?appCode=${role.appCode}"> 批量删除  </a> 
 			</li> 
 		</@security.hasPermission>
	</@layout.override>
	
	<@widget.dataGrid name="usergrid" dataSource=page title="角色列表" toolbar=true>
		<@widget.dataRow name="entity" type="iterator" renderHeader=true>
		<@widget.dataField name="ids" label="全选" render="checkbox" value="id"/>
			<@widget.dataField name="name" label="角色名"/>
			<@widget.dataField name="code.name" label="角色类型"/>
			<@widget.dataField name="status.label" label="状态"/>
			
			<@widget.dataField name="createTime" label="创建时间" dataFormat="yyyy-MM-dd"/>
			<@widget.dataField name="operation" label="操作" render="html">
				<@security.hasPermission code="plugin:AdminModule$AppRole$Edit">
				<a href="${pluginConfig.baseURL}/app/role/${entity.id}/edit" class="btn">编辑</a>
				</@security.hasPermission>
				<@security.hasPermission code="plugin:AdminModule$AppRole$Delete">
					<#list apps as app>
					<a href="${pluginConfig.baseURL}/app/permissionBind/${app.code}/${entity.id}" class="btn" data-confirm="false">${app.name }权限</a>
					</#list>
				</@security.hasPermission>
			</@widget.dataField>
		</@widget.dataRow>
	</@widget.dataGrid>

</div>
</div>

  </@override>
  
</@extends>
