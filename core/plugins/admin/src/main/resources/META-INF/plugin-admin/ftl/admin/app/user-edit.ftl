
<@extends>	
	
	<@override name="title">
		编辑 User ${user.id} 
	</@override>
	
	
 	<@override name="main-content">
		
 	<@widget.form name="user" action="${pluginConfig.baseURL}/app/user/${user.id}" method="put" label="编辑用户">
 		<#include "${pluginConfig.templateBasePath}/admin/app/user-form.ftl"/>
 	</@widget.form>
 	
  </@override>
  
</@extends>