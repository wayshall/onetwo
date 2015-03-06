
<@extends>	

	
	<@override name="title">
		新建 字典
	</@override>
	
 	<@override name="main-content">
 
 	<@widget.form name="dictionary" action="${pluginConfig.baseURL}/data/dictionary" method="post" label="新增Dictionary">
 		<#include "${pluginConfig.templateBasePath}/data/dictionary-form.ftl"/>
 	</@widget.form>
	
  </@override>
  
</@extends>