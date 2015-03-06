
<@extends>	
	
	<@override name="title">
		编辑 Role ${role.id} 
	</@override>
	
 	<@override name="main-content">
 	
 	<@widget.form name="role" action="${pluginConfig.baseURL}/app/role/${role.id}" method="put" label="编辑角色">
 		<#include "${pluginConfig.templateBasePath}/admin/app/role-form.ftl"/>
 	</@widget.form>
	
  </@override>
  
</@extends>