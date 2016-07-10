
<@extends>	

	
	<@override name="title">
		编辑字典
	</@override>
	
 	<@override name="main-content">
 
 	<@widget.form name="dictionary" action="${pluginConfig.baseURL}/data/dictionary/${id}" method="put" label="编辑字典">
 		<#include "${pluginConfig.templateBasePath}/data/dictionary-form.ftl"/>
 	</@widget.form>
	
  </@override>
  
</@extends>