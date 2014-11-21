
<@extends>	

	
	<@override name="title">
		User列表
	</@override>
	
 <@override name="main-content">

<ul class="nav nav-tabs" id="appTabs"> 	
	<#list apps as app>
		<#if app.code==user.appCode>
 			<li class="active"><a href="#appUserTab"  data-toggle="tab">${app.name }</a></li>
		<#else>
 			<li><a href="${pluginConfig.baseURL}/app/user?appCode=${app.code}"  >${app.name }</a></li>
		</#if>
	</#list>
</ul>

<div class="tab-content">
   <div id="appUserTab" class="tab-pane active">	
   
	<@layout.override name="grid_toolbar">
	    <@security.hasPermission  code="plugin:AdminModule$AppUser$New">
			<li><a href="${pluginConfig.baseURL}/app/user/new?appCode=${user.appCode}"> 新 建  </a></li>
		</@security.hasPermission>
	    <@security.hasPermission  code="plugin:AdminModule$AppUser$Delete">
 			<li><a data-method="delete" class="dg-toolbar-button-delete" href="${pluginConfig.baseURL}/app/user?appCode=${user.appCode}" >批量删除</a></li>
 		</@security.hasPermission>
	</@layout.override>
	
	<@widget.dataGrid name="user" dataSource=page title="用户列表" toolbar=true searchForm=true ajaxSupported=true >
		<@widget.dataRow name="entity" type="iterator" renderHeader=true>
			<@widget.dataField name="ids"  render="checkbox" value="id" exportable="false" label="全选"/>
			<@widget.dataField name="userName" label="用户名" search="true" />
			<@widget.dataField name="nickName" label="昵称" />
			<@widget.dataField name="email" label="电子邮件" />
			<@widget.dataField name="status.label" label="状态"  search="status" searchFieldType="select" searchItems=userStatus searchItemLabel="label" searchItemValue="value"/>
			
			<@widget.dataField name="createTime" label="创建时间" dataFormat="yyyy-MM-dd"/>
			<@widget.dataField name="operation" label="操作" render="html" exportable="false">
			<#--
				<a href="${pluginConfig.baseURL}/app/user/${entity.id}/edit" >编辑</a>
				-->
				<a href="${pluginConfig.baseURL}/app/user-role/${entity.id}" >分配角色</a>
			</@widget.dataField>
		</@widget.dataRow>
	</@widget.dataGrid>
	
  </div>
</div>
	
  </@override>
  
</@extends>
