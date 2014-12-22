
<@extends>	

	
	<@override name="title">
		新建 字典类型
	</@override>
	
 	<@override name="main-content">
 
 	<widget.form name="dictionary" action="${pluginConfig.baseURL}/data/dictionary-type" method="post" label="新增Dictionary">
 		<%@ include file="dictionary-form.jsp" %>
 		<#include "${pluginConfig.templateBasePath}/data/dictionary-type-form.ftl"/>
 	</widget.form>
	
  </@override>
  
</@extends>