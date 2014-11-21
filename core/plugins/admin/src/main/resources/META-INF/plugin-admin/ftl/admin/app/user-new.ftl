
<@extends>	

	
	<@override name="title">
		新建 User
	</@override>
	
 	<@override name="main-content">
 
 
 	<@widget.form name="user" action="${pluginConfig.baseURL}/app/user" method="post" label="新增用户">
 		<#include "${pluginConfig.templateBasePath}/admin/app/user-form.ftl"/>
 	</@widget.form>
 	
  </@override>
  
</@extends>