
<@extends>	

	
	<@override name="title">
		新建 Role
	</@override>
	
 	<@override name="main-content">
 
 	<@widget.form name="role" action="${pluginConfig.baseURL}/app/role" method="post" label="新增角色">
 		<#include "${pluginConfig.templateBasePath}/admin/app/role-form.ftl"/>
 	</@widget.form>
	
	
  </@override>
  
</@extends>